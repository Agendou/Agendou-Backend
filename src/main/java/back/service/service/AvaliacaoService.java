package back.service.service;

import back.domain.dto.response.AvaliacaoResponseDTO;
import back.domain.mapper.AvaliacaoMapper;
import back.domain.repository.AvaliacaoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class AvaliacaoService {

    private static final String API_KEY = "f1ce01a6a5ceb2da73b862c3ca4a7d2b";
    private static final Logger logger = LoggerFactory.getLogger(AvaliacaoService.class);

    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    public ResponseEntity<?> enviarAvaliacao(AvaliacaoRequestDTO avaliacaoRequest) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            var body = new JSONObject();
            body.put("form_id", avaliacaoRequest.getJfId());
            body.put("answers", new JSONObject().put("control_star_rating", avaliacaoRequest.getEstrelas()));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.jotform.com/form/" + avaliacaoRequest.getJfId() + "/submissions?apiKey=" + API_KEY))
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.error("Erro ao enviar avaliação: {} - {}", response.statusCode(), response.body());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Erro ao enviar a avaliação. Verifique os dados fornecidos.");
            }

            logger.info("Avaliação enviada com sucesso: {}", avaliacaoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Erro ao enviar avaliação", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar a avaliação.");
        }
    }

    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoes() {
        try {
            List<AvaliacaoResponseDTO> avaliacaoResponseDTOs = new ArrayList<>();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.jotform.com/user/forms?apiKey=" + API_KEY))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var formsArray = new JSONObject(response.body()).getJSONArray("content");

            for (int i = 0; i < formsArray.length(); i++) {
                String formId = formsArray.getJSONObject(i).getString("id");
                int estrelas = (int) Math.round(getStarRating(formId));

                Avaliacao avaliacao = new Avaliacao(null, formId, estrelas);
                avaliacaoRepository.save(avaliacao);
                avaliacaoResponseDTOs.add(avaliacaoMapper.toDTO(avaliacao));
            }

            avaliacaoResponseDTOs.sort((a, b) -> Integer.compare(b.getEstrelas(), a.getEstrelas()));

            return ResponseEntity.ok(avaliacaoResponseDTOs);
        } catch (Exception e) {
            logger.error("Erro ao listar avaliações", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar as avaliações.");
        }
    }

    private double getStarRating(String formId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.jotform.com/form/" + formId + "/submissions?apiKey=" + API_KEY))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray submissionsArray = new JSONObject(response.body()).getJSONArray("content");

        int totalStars = 0;
        int count = 0;

        for (int i = 0; i < submissionsArray.length(); i++) {
            JSONObject answers = submissionsArray.getJSONObject(i).optJSONObject("answers");
            if (answers == null) continue;

            for (String key : answers.keySet()) {
                JSONObject answer = answers.getJSONObject(key);
                if ("control_rating".equals(answer.optString("type")) && answer.has("answer")) {
                    try {
                        totalStars += Integer.parseInt(answer.getString("answer"));
                        count++;
                    } catch (NumberFormatException e) {
                        logger.warn("Erro ao converter estrelas para inteiro");
                    }
                }
            }
        }

        return count > 0 ? (double) totalStars / count : 0.0;
    }

    public int calcularMediaEstrelas() {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAll();
        if (avaliacoes.isEmpty()) {
            return 0;
        }

        int totalEstrelas = avaliacoes.stream().mapToInt(Avaliacao::getEstrelas).sum();
        double media = (double) totalEstrelas / avaliacoes.size();

        return (int) Math.round(media);
    }

}