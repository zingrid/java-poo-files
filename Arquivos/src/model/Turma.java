package model;

import java.util.ArrayList;
import java.util.Collections;

public class Turma {
	private ArrayList<Aluno> listaAlunos = new ArrayList<Aluno>();
	
	public void adicionarAluno(Aluno a) {
		boolean existeAluno = false;
		for(Aluno aluno : listaAlunos) {
			if(aluno.getNome().equals(a.getNome())) {
				existeAluno = true;
				break;
			}
		}
		
		if(!existeAluno) {
			listaAlunos.add(a);
		}
	}

	public ArrayList<Aluno> getListaAlunos() {
		return listaAlunos;
	}

	public void setListaAlunos(ArrayList<Aluno> listaAlunos) {
		this.listaAlunos = listaAlunos;
	}
	
	public void calcularNota(String gabaritoOficial) {
		char[] vetorGabarito = gabaritoOficial.toCharArray();
		for(Aluno aluno : listaAlunos) {
			int acertos = 0;
			char[] vetorRespostas = aluno.getRespostas().toCharArray();
			if(!(aluno.getRespostas().equals("VVVVVVVVVV")) && !(aluno.getRespostas().equals("FFFFFFFFFF"))) {
				for(int i=0; i<vetorRespostas.length; i++) {
					if(vetorRespostas[i] == vetorGabarito[i]) {
						acertos++;
					}
				}
			}
			aluno.setNota(acertos);
		}
	}
	
	public ArrayList<Aluno> ordenarPorNome() {
		ArrayList<String> nomesOrdenados = new ArrayList<String>();
		for(Aluno a : listaAlunos)
			nomesOrdenados.add(a.getNome());
		Collections.sort(nomesOrdenados);
		
		ArrayList<Integer> notas = new ArrayList<Integer>();
		for(String nome : nomesOrdenados) {
			for(Aluno a : listaAlunos) {
				if(nome.equals(a.getNome())) {
					notas.add(a.getNota());
				}
			}
		}
		
		ArrayList<Aluno> alunosPorNome = new ArrayList<Aluno>();
		for(int i=0; i<notas.size(); i++) {
			Aluno aluno = new Aluno(notas.get(i), nomesOrdenados.get(i));
			alunosPorNome.add(aluno);
		}
		
		return alunosPorNome;
	}
	
	public ArrayList<Aluno> ordenarPorNota() {
		ArrayList<Aluno> alunosPorNome = ordenarPorNome();
		
		int[] notas = new int[alunosPorNome.size()];
		String[] nomes = new String[alunosPorNome.size()];
		
		for(int i=0; i<notas.length; i++) {
			notas[i] = alunosPorNome.get(i).getNota();
			nomes[i] = alunosPorNome.get(i).getNome();
		}
		
		for(int i=0; i<notas.length; i++) {
			for(int j=0; j<notas.length-1; j++) {
				if(notas[j] < notas[j+1]) {
					int temp = notas[j];
					String nomeTemp = nomes[j];
					
					notas[j] = notas[j+1];
					nomes[j] = nomes[j+1];
					
					notas[j+1] = temp;
					nomes[j+1] = nomeTemp;
				}
			}
		}
		
		ArrayList<Aluno> alunosPorNota = new ArrayList<Aluno>();
		for(int i=0; i<notas.length; i++) {
			Aluno aluno = new Aluno(notas[i], nomes[i]);
			alunosPorNota.add(aluno);
		}
		
		return alunosPorNota;
	}
	
}
