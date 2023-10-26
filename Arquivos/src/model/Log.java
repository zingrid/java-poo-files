package model;

public class Log {
	public void telaMenu() {
		System.out.println("===================================");
		System.out.println("Digite a opção desejada:");
		System.out.println("===================================");
		System.out.println("0 - Sair\n1 - Criar Disciplina\n2 - Inserir Dados da Turma\n3 - Gerar Resultado");
		System.out.println("===================================");
	}
	
	public void telaDisciplina() {
		System.out.println("===================================");
		System.out.println("Disciplinas:");
		System.out.println("===================================");
	}
	
	public void telaOrdemAlfabetica() {
		System.out.println("===================================");
		System.out.println("RESULTADOS EM ORDEM ALFABÉTICA");
		System.out.println("NOTA\tNOME");
		System.out.println("===================================");
	}
	
	public void telaOrdemDecrescente() {
		System.out.println("===================================");
		System.out.println("RESULTADOS EM ORDEM DECRESCENTE");
		System.out.println("NOTA\tNOME");
		System.out.println("===================================");
	}
}
