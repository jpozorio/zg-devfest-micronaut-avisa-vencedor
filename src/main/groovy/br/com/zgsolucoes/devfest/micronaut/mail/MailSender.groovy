package br.com.zgsolucoes.devfest.micronaut.mail

import br.com.zgsolucoes.devfest.micronaut.sorteio.ResultadoSorteio
import com.sendgrid.*

class MailSender {

    ResultadoSorteio resultadoSorteio

    final static String conteudo = """
<div>
    <div>
        <a href="https://materiais.zgsolucoes.com.br/zgteam">Confira nossas vagas</a>
    </div>

    <div>
        <a href="https://anchor.fm/zgsolucoes">ZG Cast</a>
        <a href="https://www.deezer.com/us/show/1206822">ZG Cast - Deezer</a>
    </div>

    <div>
        <a href="https://zgsolucoes.com.br/">Conheça nosso site</a>
    </div>

    <div>
        <a href="https://linktr.ee/zgsolucoes">Linktree</a>
    </div>
</div>
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
        Content content = new Content("text/html", "<h2>Parabéns! Você foi o vencedor!</h2>".concat(conteudo))

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
        Content content = new Content("text/html", "<h2>Não foi dessa vez</h2>".concat(conteudo))
        String subject = "Que pena, não foi dessa vez."

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
