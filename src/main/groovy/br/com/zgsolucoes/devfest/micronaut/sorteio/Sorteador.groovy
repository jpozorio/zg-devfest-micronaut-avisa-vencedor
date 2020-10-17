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
        final String sendGridEnv = System.getenv('SENDGRID_ENV')
        final List<String> listaEmails = this.emails.readLines().collect { String email -> email.trim() }
        listaEmails.remove(sendGridEnv)

        Collections.shuffle(listaEmails)
        final ResultadoSorteio resultadoSorteio = new ResultadoSorteio()
        final String vencedor = listaEmails.remove(0)
        resultadoSorteio.vencedor = vencedor
        resultadoSorteio.naoVencedores = listaEmails
        return resultadoSorteio
    }

    String sorteiaEAvisa() {
        final String sendGridEnv = System.getenv('SENDGRID_ENV')
        final ResultadoSorteio resultadoSorteio = sorteiaVencedor()
        if (emails.contains(sendGridEnv)) {
            MailSender mailSender = new MailSender(resultadoSorteio)
            mailSender.avisaTodos()
            return resultadoSorteio.vencedor
        } else {
            return resultadoSorteio.vencedor.replaceAll('.*@', '---')
        }
    }
}
