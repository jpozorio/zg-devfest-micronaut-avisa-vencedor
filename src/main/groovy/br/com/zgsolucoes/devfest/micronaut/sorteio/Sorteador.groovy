package br.com.zgsolucoes.devfest.micronaut.sorteio

import br.com.zgsolucoes.devfest.micronaut.mail.MailSender
import groovy.transform.CompileStatic

@CompileStatic
class Sorteador {

	String emails

	Sorteador(String emails) {
		this.emails = emails
	}

	private ResultadoSorteio sorteiaVencedor() {
		final List<String> listaEmails = this.emails.readLines().collect { String email -> email.trim() }
		Collections.shuffle(listaEmails)
		final ResultadoSorteio resultadoSorteio = new ResultadoSorteio()
		final String vencedor = listaEmails.remove(0)
		resultadoSorteio.vencedor = vencedor
		resultadoSorteio.naoVencedores = listaEmails
		return resultadoSorteio
	}

	void sorteiaEAvisa() {
		final ResultadoSorteio resultadoSorteio = sorteiaVencedor()
		MailSender mailSender = new MailSender(resultadoSorteio)
		mailSender.avisaTodos()
	}
}
