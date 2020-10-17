package br.com.zgsolucoes.devfest.micronaut.sorteio

import br.com.zgsolucoes.devfest.micronaut.sorteio.ResultadoSorteio
import br.com.zgsolucoes.devfest.micronaut.sorteio.Sorteador
import spock.lang.Specification

class SorteadorSpec extends Specification {

	def "test escolhe vencedor"() {
		given:
		String emails = '1@abc.com\n2@abc.com\n3@abc.com'
		Sorteador sorteador = new Sorteador(emails)

		when:
		ResultadoSorteio resultadoSorteio = sorteador.sorteiaVencedor()

		then:
		resultadoSorteio.naoVencedores.size() == 2
		resultadoSorteio.vencedor.contains('abc.com')
		!resultadoSorteio.naoVencedores.contains(resultadoSorteio.vencedor)
	}

}
