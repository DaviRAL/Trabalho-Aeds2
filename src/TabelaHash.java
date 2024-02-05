


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

    public void PovoarMusicas(long quant) {
        for (long i = 1; i <= quant; i++) {
            Musicas musicas = new Musicas();
            musicas.setId(i);
            musicas.setTitulo("Heart-Shaped Box " + i);
            musicas.setArtista("Nirvana " + i);
            musicas.setEstilo("Grunge " + i);
            inserir(musicas);
        }
        System.out.println("Base de dados criada com sucesso");
    }

    public Musicas buscar(Long id) {
        try {
            int index = Hash(id);
            file.seek(index * 8);
            long pointer = file.readLong();
            while (pointer != 0) {
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
            e.printStackTrace();
        }
        return null;
    }

    public void inserir(Musicas musicas) {
        try {
            int index = Hash(musicas.getId());
            file.seek(index * 8);
            long pointer = file.readLong();
            file.seek(file.length());
            long newPointer = file.getFilePointer();
            file.writeLong(pointer); // A proxima musica que vem na lista
            file.writeLong(musicas.getId());
            file.writeUTF(musicas.getTitulo());
            file.writeUTF(musicas.getArtista());
            file.writeUTF(musicas.getEstilo());
            file.seek(index * 8);
            file.writeLong(newPointer); // Aqui o ponteiro na tabela Hash é atualizado
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remover(Long id) {
        try {
            int index = Hash(id);
            file.seek(index * 8);
            long pointer = file.readLong();
            long previousPointer = -1;
            while (pointer != 0) {
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
                    if(file.getFilePointer() == file.length()) { // Se o ponteiro for o último da lista
                        break;
                    }
                    long nextPointer = file.readLong();
                    if(file.getFilePointer() == file.length()) { // Se o ponteiro for o último da lista
                        break;
                    }
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



}
























