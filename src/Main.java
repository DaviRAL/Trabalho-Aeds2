import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        @SuppressWarnings("resource")
        Scanner leitor = new Scanner(System.in);
        System.out.println("Por favor, insira a quantidade de musicas da Tabela Hash: ");
        int aux = leitor.nextInt();
        TabelaHash tabela = new TabelaHash(aux);
        //tabela.PovoarMusicas(aux);

        while (true) {
        System.out.println("\n************************");
        System.out.println("Escolha uma opção seguir");
        System.out.println("************************");
        System.out.println("1 - Inserir uma musica ");
        System.out.println("2 - Buscar uma musica");
        System.out.println("3 - Remover uma musica");
        System.out.println("4 - Imprimir a tabela hash");
        System.out.println("5 - Sair");
        System.out.println("************************\n");
        int ax = leitor.nextInt();
        
        switch (ax) {

            case 1:
                // Inserindo uma musica nova
                System.out.println("\nDigite o id da musica que deseja inserir: ");
                int id = leitor.nextInt();
                
                Musicas newMusica = new Musicas(id, "Nutshell", "Grunge", "Alice in Chains");
                long startInsere = System.nanoTime();
                int comparacoesInsere = 0;

                try {
                    comparacoesInsere = tabela.inserir(newMusica);
                    System.out.println("\n***Musica inserida com sucesso***");
                }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
                long endInsere = System.nanoTime();

                tabela.logInsere(startInsere, endInsere, comparacoesInsere);

                break;


            case 2:
                System.out.println("\nDigite o id da musica que deseja buscar: ");
                long b = leitor.nextInt();

                long startBusca = System.nanoTime();
                Musicas music = tabela.buscar(b);
                long endBusca = System.nanoTime();

                if (music != null) {
                    System.out.println("\n***Musica encontrada***" + music);
                } else {
                    System.out.println("***ERRO! Musica nao encontrada***");
                }
                
                int comparacoesBusca = tabela.getComparacoesBusca();
                tabela.logBusca(startBusca, endBusca, comparacoesBusca);

                break;


            case 3:
                System.out.println("\nDigite o id da musica que deseja remover: ");
                long startRemove = System.nanoTime();
                long c = leitor.nextInt();
                int comparacoesRemove = tabela.remover(c);
                long endRemove = System.nanoTime();
                tabela.logRemove(startRemove, endRemove, comparacoesRemove); 

                break;


            case 4:
                HashPrinter hashPrinter = new HashPrinter("HashMusicas.dat", aux);
                hashPrinter.imprimirTabelaHash();
                //tabela.imprimirHash();

                break;


            case 5:
                System.exit(0);

            default:
                System.out.println("***Opção inválida!***");

                break;             
            }
        }
        
    }
}
