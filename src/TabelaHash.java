


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TabelaHash{
    private RandomAccessFile file;
    private int tam;


    public TabelaHash(int tam) {
        this.tam = tam;
        try {
            file = new RandomAccessFile("HashMusicas.dat", "rw");
            file.setLength(tam * 8); // São utilziados ponteiros de 8 bytes para as entradas na tabela Hash
            for (int i = 0; i < tam; i++) {
                file.writeLong(0); // Cada entrada é inicializada com um ponteiro nulo
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int Hash(Long id) {
        return (int) (id % tam);
    }

    private int comparacoesBusca = 0;
    private int comparacoesRemove = 0;
    private int comparacoesInsere = 0;

    public int getComparacoesBusca() {
    return comparacoesBusca;
    }

    public int getComparacoesRemove() {
    return comparacoesRemove;
    }

    public int getComparacoesInsere() {
        return comparacoesInsere;
    }

    public void PovoarMusicas(long quant) {
        try {
            for (long i = 1; i <= quant; i++) {
                Musicas musicas = new Musicas();
                musicas.setId(i);
                musicas.setTitulo("Heart-Shaped Box");
                musicas.setArtista("Nirvana");
                musicas.setEstilo("Grunge");
                inserir(musicas);
            }
            System.out.println("\n***Base de dados criada com sucesso***");
        }
        catch (IOException e) {
            System.out.println("\n***Erro inesperado ao povoar***" + e.getMessage());
        }
    }

    public Musicas buscar(Long id) {
        long start = System.nanoTime();
        int comparacoes = 0;
        try {
            int index = Hash(id);
            file.seek(index * 8);
            long pointer = file.readLong();
            while (pointer != 0) {
                comparacoes++;
                file.seek(pointer);
                long nextPointer = file.readLong();
                long movieId = file.readLong();
                if (movieId == id) {
                    String titulo = file.readUTF();
                    String artista = file.readUTF();
                    String estilo = file.readUTF();
                    Musicas musicas = new Musicas();
                    musicas.setId(id);
                    musicas.setTitulo(titulo);
                    musicas.setArtista(artista);
                    musicas.setEstilo(estilo);
                    long end = System.nanoTime();
                    logBusca(start, end, comparacoes);
                    return musicas;
                }
                pointer = nextPointer;
            }
        } catch (IOException e) {
            e.getMessage();
        }
        
        return null;
    }

    public void inserir(Musicas musicas) throws IOException{
        long start = System.nanoTime();
        int index = Hash(musicas.getId());
        file.seek(index * 8); //pula o ponteiro para próxima musica(8 bytes)
        long pointer = file.readLong();
        int comparacoes = 0;
    
        while(pointer != 0) {
            comparacoes++;
            file.seek(pointer + 8);
            long id = file.readLong();
            if (id == musicas.getId()) {
                throw new IOException("***ERRO! Musica com ID ja existente***");
            }
            file.seek(pointer);
            pointer = file.readLong();
        }

        file.seek(file.length());
        long newPointer = file.getFilePointer();
        file.writeLong(pointer); // A proxima musica que vem na lista
        file.writeLong(musicas.getId());
        file.writeUTF(musicas.getTitulo());
        file.writeUTF(musicas.getArtista());
        file.writeUTF(musicas.getEstilo());
        file.seek(index * 8);
        file.writeLong(newPointer); // Aqui o ponteiro na tabela Hash é atualizado
        long end = System.nanoTime();
        logInsere(start, end, comparacoes);
        

    }
   

    public void remover(Long id) {
        int comparacoes = 0;
        try {
            int index = Hash(id);
            file.seek(index * 8);
            long pointer = file.readLong();
            long previousPointer = -1;
            while (pointer != 0) {
                comparacoes++;
                file.seek(pointer);
                long nextPointer = file.readLong();
                long movieId = file.readLong();
                if (movieId == id) {
                    if (previousPointer == -1) { // A musica no início da lista
                        file.seek(index * 8);
                        file.writeLong(nextPointer);
                    } else { // A musica que está no meio ou no final da lista
                        file.seek(previousPointer);
                        file.writeLong(nextPointer);
                    }
                    break;
                }
                previousPointer = pointer;
                pointer = nextPointer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Musicas> lerMusicas() {
        List<Musicas> musica = new ArrayList<>();
        try {
            file.seek(0);
            while (file.getFilePointer() < file.length()) {
                long pointer = file.readLong();
                while (pointer != 0) {
                    file.seek(pointer);
                    long nextPointer = file.readLong();
                    long id = file.readLong();
                    String titulo = file.readUTF();
                    String artista = file.readUTF();
                    String estilo = file.readUTF();
                    Musicas musicas = new Musicas();
                    musicas.setId(id);
                    musicas.setTitulo(titulo);
                    musicas.setArtista(artista);
                    musicas.setEstilo(estilo);
                    musica.add(musicas);
                    pointer = nextPointer;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return musica;
    }

    public void logInsere(long start, long end, int comparacoes) {
        long time = end - start;
        String logMessage = "*** INSERE ***";
        String logMessage2 = "Tempo de execução de INSERÇÃO > " + time + " milisegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("\n***Erro ao escrever no arquivo de log.txt***");
            System.out.println(e.getMessage());
        }
    }

    public void logBusca(long start, long end, int comparacoes) {
        long time = end - start;
        String logMessage = "*** BUSCA ***";
        String logMessage2 = "Tempo de execução de BUSCA > " + time + " milisegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("\n***Erro ao escrever no arquivo de log.txt***");
            System.out.println(e.getMessage());
        }
    }
    public void logRemove(long start, long end, int comparacoes) {
        long time = end - start;
        String logMessage = "*** REMOVE ***";
        String logMessage2 = "Tempo de execução de REMOÇÃO > " + time + " milisegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("\n***Erro ao escrever no arquivo de log.txt***");
            System.out.println(e.getMessage());
        }
    }
}
























