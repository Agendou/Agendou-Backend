package back.service.service;

import back.domain.dto.request.AvaliacaoRequestDTO;
import back.domain.dto.response.AvaliacaoResponseDTO;
import back.domain.mapper.AvaliacaoMapper;
import back.domain.model.Avaliacao;
import back.domain.repository.AvaliacaoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvaliacaoService {
    private final String API_KEY = "f1ce01a6a5ceb2da73b862c3ca4a7d2b";

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public void submitFormRating(AvaliacaoRequestDTO avaliacaoRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject body = new JSONObject();
        body.put("form_id", avaliacaoRequest.getJfId());
        body.put("answers", new JSONObject() {{
            put("control_star_rating", avaliacaoRequest.getEstrelas());
        }});

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.jotform.com/form/" + avaliacaoRequest.getJfId() + "/submissions?apiKey=" + API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro ao enviar a avaliação. Código: " + response.statusCode() + ", Mensagem: " + response.body());
        }
    }

    public List<AvaliacaoResponseDTO> getFormRatings() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.jotform.com/user/forms?apiKey=" + API_KEY))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray formsArray = new JSONObject(response.body()).getJSONArray("content");
        List<AvaliacaoResponseDTO> avaliacaoResponseDTOs = new ArrayList<>();

        for (int i = 0; i < formsArray.length(); i++) {
            JSONObject formJson = formsArray.getJSONObject(i);
            String formId = formJson.getString("id");

            int starRating = getStarRating(formId);
            Avaliacao avaliacao = new Avaliacao(null, formId, starRating);
            avaliacaoRepository.save(avaliacao);

            avaliacaoResponseDTOs.add(AvaliacaoMapper.toDTO(avaliacao));
        }


        mergeSort(avaliacaoResponseDTOs, 0, avaliacaoResponseDTOs.size() - 1);

        return avaliacaoResponseDTOs;
    }

    private int getStarRating(String formId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.jotform.com/form/" + formId + "/submissions?apiKey=" + API_KEY))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray submissionsArray = new JSONObject(response.body()).getJSONArray("content");

        int totalStars = 0;
        int count = 0;

        for (int i = 0; i < submissionsArray.length(); i++) {
            JSONObject submissionJson = submissionsArray.getJSONObject(i);
            JSONObject answers = submissionJson.getJSONObject("answers");

            System.out.println("Respostas da submissão " + (i + 1) + ": " + answers.toString());

            for (String key : answers.keySet()) {
                JSONObject answer = answers.getJSONObject(key);

                if (answer.has("type") && "control_rating".equals(answer.getString("type")) && answer.has("answer")) {
                    String starString = answer.optString("answer", "0");
                    System.out.println("Valor da estrela encontrado: " + starString);

                    try {
                        int starRating = Integer.parseInt(starString);
                        totalStars += starRating;
                        count++;
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter o valor de estrelas para int: " + starString);
                    }
                } else {
                    System.out.println("Campo ignorado: " + key + " (não é `control_rating` ou sem `answer`)");
                }
            }
        }

        System.out.println("Total de estrelas: " + totalStars + ", Contagem de avaliações: " + count);
        return count > 0 ? totalStars / count : -1;
    }




    private void mergeSort(List<AvaliacaoResponseDTO> avaliacoes, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            mergeSort(avaliacoes, left, middle);
            mergeSort(avaliacoes, middle + 1, right);

            merge(avaliacoes, left, middle, right);
        }
    }

    private void merge(List<AvaliacaoResponseDTO> avaliacoes, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        AvaliacaoResponseDTO[] leftArray = new AvaliacaoResponseDTO[n1];
        AvaliacaoResponseDTO[] rightArray = new AvaliacaoResponseDTO[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = avaliacoes.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = avaliacoes.get(middle + 1 + j);
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].getEstrelas() <= rightArray[j].getEstrelas()) {
                avaliacoes.set(k, leftArray[i]);
                i++;
            } else {
                avaliacoes.set(k, rightArray[j]);
                j++;
            }
            k++;
        }

        while (i < n1) {
            avaliacoes.set(k, leftArray[i]);
            i++;
            k++;
        }

        while (j < n2) {
            avaliacoes.set(k, rightArray[j]);
            j++;
            k++;
        }
    }
}
