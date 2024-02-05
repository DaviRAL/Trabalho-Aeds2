import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Scanner leitor = new Scanner(System.in);
        System.out.println("Por favor, insira a quantidade de musicas da Tabela Hash: ");
        int aux = leitor.nextInt();
        TabelaHash tabela = new TabelaHash(aux);
        while (true) {
        System.out.println("\n************************");
        System.out.println("Escolha uma opção seguir");
        System.out.println("************************");
        System.out.println("1 - Inserir uma musica ");
        System.out.println("2 - Buscar uma musica");
        System.out.println("3 - Remover uma musica");
        //System.out.println("4 - Listar musicas");
        System.out.println("4 - Sair");
        System.out.println("************************\n");
        int ax = leitor.nextInt();
        
        switch (ax) {

            case 1:
                // Inserindo uma musica nova
                System.out.println("\nDigite o id da musica que deseja inserir: ");
                int a = leitor.nextInt();
                Musicas newMusica = new Musicas(a, "Nutshell", "Grunge", "Alice in Chains");
                tabela.inserir(newMusica);
                break;

            case 2:
                // Search for a Musica with id 3
                System.out.println("\nDigite o id da musica que deseja buscar: ");
                long b = leitor.nextInt();
                Musicas music = tabela.buscar(b);
                if (music != null) {
                    System.out.println("\n***Musica encontrada***" + music);
                } else {
                    System.out.println("ERRO! Musica nao encontrada.");
                }
                break;

            case 3:
                System.out.println("\nDigite o id da musica que deseja remover: ");
                long c = leitor.nextInt();
                tabela.remover(c);
                break;

/*             case 4:
                List<Musicas> musicas = tabela.lerMusicas();
                for(Musicas m : musicas) {
                    System.out.println(m);
                    System.out.println();
                }
                break; */

            case 4:
                System.exit(0);
                default:
                System.out.println("Opção inválida!");
                    break;
        }
    
        }
        
    }
}
