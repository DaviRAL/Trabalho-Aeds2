import java.io.IOException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        @SuppressWarnings("resource")
        Scanner leitor = new Scanner(System.in);
        System.out.println("Por favor, insira a quantidade de musicas da Tabela Hash: ");
        int aux = leitor.nextInt();
        TabelaHash tabela = new TabelaHash(aux);
        tabela.PovoarMusicas(aux);

        while (true) {
        System.out.println("\n************************");
        System.out.println("Escolha uma opção seguir");
        System.out.println("************************");
        System.out.println("1 - Inserir uma musica ");
        System.out.println("2 - Buscar uma musica");
        System.out.println("3 - Remover uma musica");
        System.out.println("4 - Sair");
        System.out.println("************************\n");
        int ax = leitor.nextInt();
        
        switch (ax) {

            case 1:
            
            // Inserindo uma musica nova
            System.out.println("\nDigite o id da musica que deseja inserir: ");
            int id = leitor.nextInt();
            Musicas newMusica = new Musicas(id, "Nutshell", "Grunge", "Alice in Chains");
            try {
                tabela.inserir(newMusica);
                System.out.println("\n***Musica inserida com sucesso***");
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
            
        
            break;

            case 2:
                System.out.println("\nDigite o id da musica que deseja buscar: ");
                long b = leitor.nextInt();
                Musicas music = tabela.buscar(b);

                
                if (music != null) {
                    System.out.println("\n***Musica encontrada***" + music);
                } else {
                    System.out.println("***ERRO! Musica nao encontrada***");
                }
                
                
               

                break;

            case 3:
            System.out.println("\nDigite o id da musica que deseja remover: ");
            long startRemove = System.currentTimeMillis();
            long c = leitor.nextInt();
            tabela.remover(c);
            long endRemove = System.currentTimeMillis();
            int comparacoesRemove = tabela.getComparacoesRemove(); // Adicione esta linha
            tabela.logRemove(startRemove, endRemove, comparacoesRemove); // Modifique esta linha

            break;
                
            case 4:
                System.exit(0);

            default:
                System.out.println("Opção inválida!");
                break;             
            }
        }
        
    }
}
