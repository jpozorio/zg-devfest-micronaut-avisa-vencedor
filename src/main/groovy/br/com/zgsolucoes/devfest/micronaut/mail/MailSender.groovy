package br.com.zgsolucoes.devfest.micronaut.mail

import br.com.zgsolucoes.devfest.micronaut.sorteio.ResultadoSorteio
import com.sendgrid.*

class MailSender {

	ResultadoSorteio resultadoSorteio

	final String conteudo = """
Confira nossas vagas:    https://materiais.zgsolucoes.com.br/zgteam

ZG Cast: https://anchor.fm/zgsolucoes
https://www.deezer.com/us/show/1206822

ZG Soluções: https://zgsolucoes.com.br/

Linktree: https://linktr.ee/zgsolucoes
"""

	MailSender(ResultadoSorteio resultadoSorteio) {
		this.resultadoSorteio = resultadoSorteio
	}

	void avisaTodos() {
		avisaVencedor()
		avisaNaoVencedores()
	}

	private void avisaVencedor() {
		Email from = new Email("joaopaulo@zgsolucoes.com.br", 'João Paulo Ozório')

		String subject = "Parabéns! Você foi o vencedor!"
		Email to = new Email(resultadoSorteio.vencedor)
		Content content = new Content("text/plain", "Parabéns! Você foi o vencedor!".concat(conteudo))

		Mail mail = new Mail(from, subject, to, content)

		Personalization personalization = mail.personalization.get(0)
		personalization.addCc(new Email('joaopaulo@zgsolucoes.com.br'))

		SendGrid sg = new SendGrid(System.getenv('SENDGRID_ENV'))
		Request request = new Request()
		try {
			request.setMethod(Method.POST)
			request.setEndpoint("mail/send")
			request.setBody(mail.build())
			sg.api(request)
		} catch (IOException ex) {
			throw ex
		}
	}

	private void avisaNaoVencedores() {
		Email from = new Email("joaopaulo@zgsolucoes.com.br", 'João Paulo Ozório')
		Content content = new Content("text/plain", "Você não foi sorteado.".concat(conteudo))
		String subject = "Você não foi sorteado"

		resultadoSorteio.naoVencedores.each { String email ->
			Email to = new Email(email)

			Mail mail = new Mail(from, subject, to, content)

			SendGrid sg = new SendGrid(System.getenv('SENDGRID_ENV'))
			Request request = new Request()
			try {
				request.setMethod(Method.POST)
				request.setEndpoint("mail/send")
				request.setBody(mail.build())
				sg.api(request)
			} catch (IOException ex) {
				ex.printStackTrace()
			}
		}
	}

}
