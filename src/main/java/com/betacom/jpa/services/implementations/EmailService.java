package com.betacom.jpa.services.implementations;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.betacom.jpa.models.DettaglioOrdine;
import com.betacom.jpa.models.Ordine;
import com.betacom.jpa.models.Utente;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Infrastruttura condivisa
// Invia le email dello store (ricevuta d'ordine, reset password). L'invio non deve mai
// bloccare l'operazione che lo richiede: eventuali errori vengono solo loggati.
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final JavaMailSender mailSender;

	@Value("${app.mail.from:no-reply@hakustore.local}")
	private String from;

	@Value("${app.frontend-url:http://localhost:4200}")
	private String frontendUrl;

	public void inviaConfermaOrdine(Utente utente, Ordine ordine) {
		String oggetto = "Conferma ordine #" + ordine.getIdOrdine() + " - Hakustore";
		StringBuilder righe = new StringBuilder();
		for (DettaglioOrdine r : ordine.getRighe()) {
			String nomeProdotto = r.getVariante().getProdotto().getNome();
			String varianteDesc = descrizioneVariante(r);
			righe.append("<tr>")
					.append("<td style=\"padding:6px 10px;border-bottom:1px solid #eee;\">").append(nomeProdotto)
					.append(varianteDesc.isEmpty() ? "" : " (" + varianteDesc + ")").append("</td>")
					.append("<td style=\"padding:6px 10px;border-bottom:1px solid #eee;text-align:center;\">").append(r.getQuantita()).append("</td>")
					.append("<td style=\"padding:6px 10px;border-bottom:1px solid #eee;text-align:right;\">").append(r.getPrezzoUnitario()).append(" &euro;</td>")
					.append("</tr>");
		}

		String html = "<html><body style=\"font-family:Arial,sans-serif;color:#222;\">"
				+ "<h2>Grazie per il tuo ordine, " + utente.getNome() + "!</h2>"
				+ "<p>Ordine <strong>#" + ordine.getIdOrdine() + "</strong> del " + ordine.getDataOrdine().format(DATA) + "</p>"
				+ "<table style=\"border-collapse:collapse;width:100%;max-width:500px;\">"
				+ "<thead><tr>"
				+ "<th style=\"text-align:left;padding:6px 10px;border-bottom:2px solid #333;\">Articolo</th>"
				+ "<th style=\"text-align:center;padding:6px 10px;border-bottom:2px solid #333;\">Qta</th>"
				+ "<th style=\"text-align:right;padding:6px 10px;border-bottom:2px solid #333;\">Prezzo</th>"
				+ "</tr></thead><tbody>" + righe + "</tbody></table>"
				+ "<p style=\"margin-top:16px;\">Totale prodotti: " + ordine.getTotaleProdotti() + " &euro;<br/>"
				+ (ordine.getValoreSconto() != null && ordine.getValoreSconto().signum() > 0
						? "Sconto applicato: -" + ordine.getValoreSconto() + " &euro;<br/>" : "")
				+ "<strong>Totale pagato: " + ordine.getTotalePagato() + " &euro;</strong></p>"
				+ "<p>Spedizione a: " + ordine.getSpedizioneVia() + ", " + ordine.getSpedizioneCap() + " " + ordine.getSpedizioneCitta()
				+ (ordine.getSpedizioneProvincia() != null ? " (" + ordine.getSpedizioneProvincia() + ")" : "")
				+ ", " + ordine.getSpedizioneNazione() + "</p>"
				+ "<p>Metodo di pagamento: " + ordine.getMetodoPagamento() + "</p>"
				+ "<p style=\"color:#777;font-size:12px;margin-top:24px;\">Questa email vale come ricevuta d'ordine ai fini fiscali: conservala.</p>"
				+ "</body></html>";

		invia(utente.getEmail(), oggetto, html);
	}

	public void inviaResetPassword(Utente utente, String token) {
		String link = frontendUrl + "/reset-password?token=" + token;
		String oggetto = "Reimposta la tua password - Hakustore";
		String html = "<html><body style=\"font-family:Arial,sans-serif;color:#222;\">"
				+ "<h2>Reimposta la password</h2>"
				+ "<p>Ciao " + utente.getNome() + ", abbiamo ricevuto una richiesta di reset della password del tuo account Hakustore.</p>"
				+ "<p><a href=\"" + link + "\" style=\"background:#222;color:#fff;padding:10px 18px;text-decoration:none;border-radius:4px;\">Reimposta password</a></p>"
				+ "<p>Il link e' valido per 30 minuti. Se non hai richiesto tu il reset, ignora questa email.</p>"
				+ "</body></html>";

		invia(utente.getEmail(), oggetto, html);
	}

	private String descrizioneVariante(DettaglioOrdine r) {
		StringBuilder sb = new StringBuilder();
		if (r.getVariante().getGusto() != null) sb.append(r.getVariante().getGusto());
		if (r.getVariante().getFormato() != null) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(r.getVariante().getFormato());
		}
		return sb.toString();
	}

	private void invia(String destinatario, String oggetto, String html) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
			helper.setFrom(from);
			helper.setTo(destinatario);
			helper.setSubject(oggetto);
			helper.setText(html, true);
			mailSender.send(msg);
		} catch (Exception e) {
			log.error("Errore invio email a {}: {}", destinatario, e.getMessage());
		}
	}

}
