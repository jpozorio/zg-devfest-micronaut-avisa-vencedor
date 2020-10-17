package br.com.zgsolucoes.devfest.micronaut.mail

import br.com.zgsolucoes.devfest.micronaut.sorteio.ResultadoSorteio
import com.sendgrid.*

class MailSender {

	ResultadoSorteio resultadoSorteio

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
		Content content = new Content("text/plain", "Parabéns! Você foi o vencedor!")

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
		Content content = new Content("text/plain", "Parabéns! Você foi o vencedor!")
		String subject = "Você não foi sorteado"

		Email to = new Email('emailVencedor')

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

}
