package model;

public class Aluno {
	private int nota;
	private String nome;
	private String respostas;
	
	public Aluno(int nota, String nome) {
		this.nota = nota;
		this.nome = nome;
	}
	
	public Aluno(String respostas, String nome) {
		this.respostas = respostas;
		this.nome = nome;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getRespostas() {
		return respostas;
	}

	public void setRespostas(String respostas) {
		this.respostas = respostas;
	}
	
}
