import java.io.*;
public class TabelaHash {

    private RandomAccessFile file;
    private int tam;
    private int contador = 0;

    private int comparacoesBusca = 0;

    public int getComparacoesBusca() {
        return comparacoesBusca;
    }

    private int Hash(Long id) {
        return (int) (id % tam);
    }

    public TabelaHash(int tam) {
        this.tam = tam;
        try {
            file = new RandomAccessFile("HashMusicas.dat", "rw");
            file.setLength(tam * 8); //São utilziados ponteiros de 8 bytes para as entradas na tabela Hash
            for (int i = 0; i < tam; i++) {
                file.writeLong(0); //Cada entrada é inicializada com um ponteiro nulo
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            int index = Hash(id);
            file.seek(index * 8);
            long pointer = file.readLong(); //ponteiro para a primeira musica na lista
            while (pointer != 0) {
                comparacoesBusca++;
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

                    return musicas;
                }
                pointer = nextPointer;
            }
        } catch (IOException e) {
            e.getMessage();
        }
        
        return null;
    }

    public boolean estaCheia() {
        return contador == tam;
    }

    public int inserir(Musicas musicas) throws IOException {

        if (estaCheia()) {
            throw new IOException("\n***ERRO! A tabela hash está cheia!***");
        }

        int index = Hash(musicas.getId());
        file.seek(index * 8); 
        long pointer = file.readLong(); //lê o ponteiro para a primeira música na lista encadeada.
        long lastPointer = 0;
        int comparacoes = 0;
    
        while (pointer != 0) { //
            comparacoes++;
            file.seek(pointer + 8);
            long id = file.readLong();
            if (id == musicas.getId()) {
                throw new IOException("\n***ERRO! Musica com ID já existente***");
            }
            file.seek(pointer);
            lastPointer = pointer;
            pointer = file.readLong();
        }
    
        file.seek(file.length());
        long newPointer = file.getFilePointer();
        file.writeLong(0); // A nova música é a última na lista, então seu ponteiro é 0
        file.writeLong(musicas.getId());
        file.writeUTF(musicas.getTitulo());
        file.writeUTF(musicas.getArtista());
        file.writeUTF(musicas.getEstilo());
    
        if (lastPointer != 0) {
            // Se lastPointer não é 0, então nós precisamos atualizar o ponteiro na última música da lista
            file.seek(lastPointer);
            file.writeLong(newPointer);
        } else {
            // Se lastPointer ainda é 0, então a lista estava vazia e nós precisamos atualizar o ponteiro no índice hash
            file.seek(index * 8);
            file.writeLong(newPointer);
        }
        
        contador++;
        return comparacoes;
    }
    
    public int remover(Long id) {
        int comparacoes = 0;
        boolean idEncontrado = false;
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
                    idEncontrado = true;
                    if (previousPointer == -1) { //executada quando a musica a ser removida é a primeira da lista
                        file.seek(index * 8);
                        file.writeLong(nextPointer);
                    } else { //executada quando a musica a ser removida não é a primeira da lista
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

        if(idEncontrado) {
            System.out.println("\n***Musica removida com sucesso!***");
        }
        else {
            System.out.println("\n***ERRO! Musica nao encontrada para remover!***");
        }

        return comparacoes;
    }

    
    public void imprimirHash() throws IOException {
        for (int i = 0; i < tam; i++) {
            System.out.println("---Hash Index " + i + "---");
            file.seek(i * 8);
            long pointer = file.readLong();
            while (pointer != 0) {
                file.seek(pointer + 8);
                long id = file.readLong();
                String titulo = file.readUTF();
                String artista = file.readUTF();
                String estilo = file.readUTF();
                System.out.println("\tID > " + id + ", Titulo > " + titulo + ", Artista > " + artista + ", Estilo > " + estilo);
                file.seek(pointer);
                pointer = file.readLong();
            }
        }
    }


    public void logInsere(long start, long end, int comparacoes) {
        long time = end - start;
        String logMessage = "*** INSERE ***";
        String logMessage2 = "Tempo de execução de INSERÇÃO > " + time + " nanossegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.println();
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
        String logMessage2 = "Tempo de execução de BUSCA > " + time + " nanossegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.println();
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
        String logMessage2 = "Tempo de execução de REMOÇÃO > " + time + " nanossegundos";
        String logMessage3 = "Número de comparações: " + comparacoes;
    
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            writer.println(logMessage);
            writer.println(logMessage2);
            writer.println(logMessage3);
            writer.println();
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("\n***Erro ao escrever no arquivo de log.txt***");
            System.out.println(e.getMessage());
        }
    }
    
}
























