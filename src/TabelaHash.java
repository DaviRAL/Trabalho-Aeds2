import java.io.*;
public class TabelaHash{
    private RandomAccessFile file;
    private int tam;


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


    public int inserir(Musicas musicas) throws IOException {
        int index = Hash(musicas.getId());
        file.seek(index * 8);
        long oldPointer = file.readLong();
        int comparacoes = 0;
    
        while (oldPointer != 0) {
            comparacoes++;
            file.seek(oldPointer + 8);
            long id = file.readLong();
            if (id == musicas.getId()) {
                throw new IOException("***ERRO! Musica com ID já existente***");
            }
            file.seek(oldPointer);
            oldPointer = file.readLong();
        }
    
        file.seek(file.length());
        long newPointer = file.getFilePointer();
        file.writeLong(oldPointer);
        file.writeLong(musicas.getId());
        file.writeUTF(musicas.getTitulo());
        file.writeUTF(musicas.getArtista());
        file.writeUTF(musicas.getEstilo());
        file.seek(index * 8);
        file.writeLong(newPointer);
    
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

    
    public void imprimirHash() {
        try {
            for (int i = 0; i < tam; i++) {
                file.seek(i * 8);
                long pointer = file.readLong();
    
                if (pointer != 0) { //verifica se o ponteiro não é nulo(indice hash não vazio)
                    System.out.printf("Hash Index %d | ", i);
    
                    while (pointer != 0) { 
                        file.seek(pointer);
                        long nextPointer = file.readLong();
                        long id = file.readLong();
                        String titulo = file.readUTF();
                        String artista = file.readUTF();
                        String estilo = file.readUTF();
                        System.out.print("ID > " + id + ", Titulo > " + titulo + ", Artista > " + artista + ", Estilo > " + estilo);
                        pointer = nextPointer;
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
























