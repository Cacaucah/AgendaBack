/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.exceptions;

/**
 * @author Ana Caroline
 *
 */
public class ErroDeAutenticacao extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */	
	public ErroDeAutenticacao(String mensagem) {
		super(mensagem);
	}

}
