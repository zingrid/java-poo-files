package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Aluno;
import model.Log;
import model.Turma;

public class Main {
	static final Log visualizar = new Log();
	private static final DecimalFormat df = new DecimalFormat("0.00");
	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) throws IOException, FileNotFoundException, InputMismatchException {
		
		// Criando o diretório disciplinas no disco local
		File diretorio = new File("C:\\Disciplinas");
		if(!(diretorio.isDirectory())) {
			diretorio.mkdir();
		}
		
		// Menu
		visualizar.telaMenu();
		String escolha = scan.next();
		
		
		while(!escolha.equals("0")) {
			switch(escolha) {
			case "1":
				String nome = lerDisciplina();

				// Cria um subdiretório com o nome da disciplina
				File subdir = new File(diretorio, nome);
				subdir.mkdir();

				System.out.println("Digite a resposta, sem separação de espaço: ");
				String linha = scan.next().toUpperCase();
				
				// Checa se digitou uma resposta que não seja exatamente 10 caracteres contendo V/F, mas que não seja um dos que dão nota zero
				while((!(isRespostaValida(linha))) || (((linha.equals("VVVVVVVVVV")) || (linha.equals("FFFFFFFFFF"))))) {
					limparBuffer();
					System.out.println("Resposta inválida!");
					System.out.println("Digite a resposta, sem separação de espaço: ");
					linha = scan.next().toUpperCase();
				}
				
				// Cria o arquivo gabarito da prova
				File gabarito = new File(subdir, nome + "Gabarito.txt");
				FileWriter fw = new FileWriter(gabarito, false);

				// Grava o input digitado no arquivo gabarito
				BufferedWriter bw = new BufferedWriter(fw);
				
				try {
					bw.write(linha);
				} catch(IOException e) {
					System.out.println("Erro na criação do arquivo");
				} finally {
					bw.close();
					fw.close();
				}
				
				break;
			case "2":
				mostrarDisciplinas(diretorio);
				String input = lerDisciplina();
				
				Turma listaAlunos = new Turma();
				if(!disciplinaExiste(diretorio, input)) {
					System.out.println("Disciplina não encontrada");
				} else {
					// Checa se a quantidade de alunos é válida, se sim, lê as respostas/nomes e grava no arquivo
					do {
						System.out.println("Digite a quantidade de alunos: ");
						try {
							int num = scan.nextInt();
							limparBuffer();
							if(num > 0) {
								for(int i=0; i<num; i++) {
									System.out.println("Digite as respostas do aluno " + (i+1) + ": ");
									String respostasAluno = scan.next().toUpperCase();
									// Testa se a pessoa digitou corretamente as respostas (exatamente 10 caracteres, somente V/F)
									while(!(isRespostaValida(respostasAluno))) {
										System.out.println("Resposta inválida!");
										System.out.println("Digite novamente as respostas: ");
										respostasAluno = scan.next().toUpperCase();
									}
									
									limparBuffer();
									System.out.println("Digite o nome do aluno " + (i+1) + ": ");
									String nomeAluno = scan.nextLine().toUpperCase();
									while(!(isNomeValido(nomeAluno))) {
										System.out.println("Nome inválido!");
										System.out.println("Digite novamente o nome do aluno: ");
										nomeAluno = scan.nextLine().toUpperCase();
									}
									
									// Garante que o nome será no formato "Adriely da Silva" e não "	Adriely		da Silva		"
									String nomeAlunoFormatado = nomeAluno.replaceAll("\\s+", " ").trim();
									
									// Cria um aluno e o adiciona à listaAlunos caso ele já não esteja lá
									Aluno aluno = new Aluno(respostasAluno, nomeAlunoFormatado);
									listaAlunos.adicionarAluno(aluno);
								}
								
								criarListaRespostas(input, listaAlunos);
								
								break;
							} else {
								// Se a pessoa digitar um número <= 0
								System.out.println("Número inválido!");
							}
						} catch (InputMismatchException e) {
							// Se a pessoa digitar algo que não é um inteiro
							System.out.println("Número inválido!");
							limparBuffer();
						}
					} while (true);
				}
				
				break;
			case "3":
				mostrarDisciplinas(diretorio);
				String nomeDisciplina = lerDisciplina();
				
				// Se a disciplina não existe, certamente o arquivo do gabarito oficial não existe
				if(!disciplinaExiste(diretorio, nomeDisciplina)) {
					System.out.println("Disciplina não encontrada");
				} else {
					System.out.println("Informe a localização do arquivo onde se encontra o gabarito oficial: ");
					String caminho = scan.next();
					String caminhoAbsoluto = "C:\\Disciplinas\\" + nomeDisciplina + "\\" + nomeDisciplina + "Gabarito.txt";
					
					// Verifica se a pessoa digitou o caminho absoluto do arquivo; se não, substitui pelo caminho absoluto do arquivo
					if(!(caminho.equals(caminhoAbsoluto))) {
						caminho = caminhoAbsoluto;
					}
					
					if(!existeArquivo(caminho)) {
						System.out.println("Arquivo não encontrado");
					} else {
						// Checar se existe o arquivo de dados da turma, "nomeDisciplina.txt"; se não, é porque a turma é vazia
						String caminhoDadosTurma = "C:\\Disciplinas\\" + nomeDisciplina + "\\" + nomeDisciplina + ".txt";
						if(!existeArquivo(caminhoDadosTurma)) {
							System.out.println("Não é possível gerar o resultado da disciplina " + nomeDisciplina + "\nTurma sem alunos");
						} else {
							// Cria o diretório resultados
							File subdir2 = new File("C:\\Disciplinas\\" + nomeDisciplina, "resultados");
							subdir2.mkdir();

							// Lê o gabarito oficial
							String respostas = null;
							try {
								FileReader fr = new FileReader(caminho); // arquivo gabarito oficial
								BufferedReader br = new BufferedReader(fr);
								try {
									respostas = br.readLine();
								} catch (IOException e) {
									System.out.println("Erro na leitura do arquivo");
								}
								br.close();
								fr.close();
							} catch (FileNotFoundException e) {
								System.out.println("Arquivo não encontrado");
							}

							Turma lista = new Turma();
							// Lê o arquivo de respostas e calcula a nota de cada aluno
							try {
								FileReader fr2 = new FileReader(caminho.replace("Gabarito.txt", ".txt")); // arquivo poo.txt
								BufferedReader br2 = new BufferedReader(fr2);
								try {
									String linhas = br2.readLine();
									while(linhas != null) {
										String[] dados = linhas.split("\t");
										Aluno aluno = new Aluno(dados[0], dados[1]);
										lista.adicionarAluno(aluno);
										lista.calcularNota(respostas);
										linhas = br2.readLine();
									}
								} catch (IOException e) {
									System.out.println("Erro na leitura do arquivo");
								} finally {
									br2.close();
									fr2.close();
								}
							} catch (FileNotFoundException e) {
								System.out.println("Arquivo não encontrado");
							}

							// Cria duas listas, uma ordenando os alunos alfabeticamente e a outra em ordem decrescente de notas
							ArrayList<Aluno> alunosPorNome = lista.ordenarPorNome();
							ArrayList<Aluno> alunosPorNota = lista.ordenarPorNota();

							// Calcula a média da turma
							double mediaTurma = calcularMedia(alunosPorNome);

							// Grava no arquivo resultadoOrdemAlfabetica.txt, que deve estar no subdir2 (pasta resultados)
							File arq = new File(subdir2, "resultadoOrdemAlfabetica.txt");
							criarArquivo(arq, alunosPorNome);

							// Grava, junto com a mediaTurma, no arquivo resultadoOrdemDecrescente.txt, que deve estar no subdir2 (pasta resultados)
							File arq2 = new File(subdir2, "resultadoOrdemDecrescente.txt");
							criarArquivo(arq2, alunosPorNota, mediaTurma);

							// Mostra na tela o conteúdo do arquivo "resultadoOrdemAlfabetica.txt"
							visualizarDados(alunosPorNome, "porNome");
							visualizarDados(alunosPorNota, "porNota");
								
							}
						}
				
				}
				
				break;
			default:
				System.out.println("Opção inválida!");
				break;
			}
			
			visualizar.telaMenu();
			escolha = scan.next();
		}
		
		System.exit(0);
	}

	public static String lerDisciplina() {
		System.out.println("Digite o nome da disciplina (sem espaços em branco entre as palavras): ");
		String nome = scan.next().toLowerCase();
		while(!isDisciplinaValida(nome)) {
			System.out.println("Nome inválido!\nDigite o nome da disciplina (sem espaços em branco entre as palavras): ");
			nome = scan.next().toLowerCase();
		}
		return nome;
	}
	
	public static void limparBuffer() {
		scan.nextLine();
	}
	
	public static double calcularMedia(ArrayList<Aluno> lista) {
		double somaTotal = 0.0;
		for(Aluno a : lista) {
			somaTotal += a.getNota();
		}
		double mediaTurma = somaTotal/lista.size();
		
		return mediaTurma;
	}
	
	public static void mostrarDisciplinas(File diretorio) {
		String[] listaDir = diretorio.list();
		visualizar.telaDisciplina();
		for(int i=0; i<listaDir.length; i++) {
			System.out.println(listaDir[i]);
		}
		System.out.println("===================================");
	}
	
	public static boolean disciplinaExiste(File diretorio, String input) {
		String[] listaDir = diretorio.list(); // lista os nomes apenas
		for(int i=0; i<listaDir.length; i++) {
			if(input.equals(listaDir[i])) {
				return true;
			}
		}
		return false; // se nenhum dos nomes dos diretórios for o que a pessoa digitou, retorna false
	}
	
	public static boolean isDisciplinaValida(String nomeDisciplina) {
		char[] vetorNome = nomeDisciplina.toCharArray();
		for(int i=0; i<vetorNome.length; i++) {
			// Se tiver pelo menos uma letra, é válida
			if(Character.isLetter(vetorNome[i])) {
				return true;
			}
		}
		return false;
	}
		
	public static boolean isRespostaValida(String linha) {
		// Checa se o input digitado é uma string de tamanho 10 composta apenas por caracteres V ou F
		if(linha.length() != 10) {
			return false;
		} else {
			// Se o tamanho da string for 10, checar se pelo menos um caractere é diferente de V ou F
			char[] vetorLinha = linha.toCharArray();
			for(int i=0; i<vetorLinha.length; i++) {
				if(!(vetorLinha[i] == 'V') && !(vetorLinha[i] == 'F')) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static boolean isNomeValido(String nome) {
		// Se o nome for apenas whitespace, então retorna falso
		if(nome.isBlank()) {
			return false;
		} 
		
		String nomeSemWhitespace = nome.replaceAll("\\s+", "");
		char[] vetorNome = nomeSemWhitespace.toCharArray();
		for(int i=0; i<vetorNome.length; i++) {
			// Se pelo menos um caractere não for letra, retorna falso
			if(!(Character.isLetter(vetorNome[i]))) {
				return false;
			}
		}
		return true;
	}
	
	public static void criarListaRespostas(String input, Turma lista) throws IOException {
		// Se o diretório foi encontrado, entrar nele e criar o arquivo com as respostas da turma
		String caminho = "C:\\Disciplinas\\" + input;
		File respostas = new File(caminho, input + ".txt");
		FileWriter fw = new FileWriter(respostas, false);
		BufferedWriter bw = new BufferedWriter(fw);
		
		ArrayList<Aluno> listaAlunos = new ArrayList<Aluno>();
		listaAlunos = lista.getListaAlunos();
		
		try {
			for(Aluno a : listaAlunos) {
				// Grava no arquivo as respostas e nome de cada aluno, separados por tab
				bw.write(a.getRespostas() + "\t" + a.getNome());
				bw.newLine();
			}
		} catch (Exception e) {
			System.out.println("Erro na criação do arquivo");
		} finally {
			bw.close();
			fw.close();
		}
	}
	
	public static boolean existeArquivo(String caminhoAbsoluto) {
		File arq = new File(caminhoAbsoluto);
		
		if(arq.isFile()) {
			return true;
		}
		
		return false;
	}
	
	public static void criarArquivo(File file, ArrayList<Aluno> lista) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			for(Aluno aluno : lista) {
				bw.write(aluno.getNota() + "\t" + aluno.getNome());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("Erro na criação do arquivo");
		} finally {
			bw.close();
			fw.close();
		}
	}
	
	public static void criarArquivo(File file, ArrayList<Aluno> lista, double media) throws IOException {		
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			for(Aluno aluno : lista) {
				bw.write(aluno.getNota() + "\t" + aluno.getNome());
				bw.newLine();
			}
			bw.write(df.format(media) + "\t" + ""); // formata a media para duas casas decimais
			bw.newLine();
		} catch (IOException e) {
			System.out.println("Erro na criação do arquivo");
		} finally {
			bw.close();
			fw.close();
		}
	}
	
	public static void visualizarDados(ArrayList<Aluno> lista, String ordenacao) {
		double media = calcularMedia(lista);
		
		if(ordenacao.equals("porNome")) {
			visualizar.telaOrdemAlfabetica();
			for(Aluno a : lista) {
				System.out.println(a.getNota() + "\t" + a.getNome());
			}
			System.out.println("===================================");
		} else {
			visualizar.telaOrdemDecrescente();
			for(Aluno a : lista) {
				System.out.println(a.getNota() + "\t" + a.getNome());
			}
			System.out.println("MÉDIA " + "\t" + df.format(media));
			System.out.println("===================================");
		}
	}
	
}
