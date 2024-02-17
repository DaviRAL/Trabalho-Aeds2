import java.io.IOException;
import java.io.RandomAccessFile;

public class HashPrinter {

    private RandomAccessFile file;
    private int tam;

    public HashPrinter(String fileName, int tam) {
        this.tam = tam;
        try {
            file = new RandomAccessFile(fileName, "r");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void imprimirTabelaHash() {
        try {
            for (int i = 0; i < tam; i++) {
                System.out.print("Hash Index " + i + " > ");
                imprimirListaEncadeada(i);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void imprimirListaEncadeada(int index) throws IOException {
        long hashPointer = index * 8; // ponteiro na tabela hash
        file.seek(hashPointer);
        long listPointer = file.readLong(); // ponteiro para a primeira música na lista encadeada
    
        int contador = 0;
    
        while (listPointer != 0) {
            contador++;
            imprimirMusica(listPointer);
    
            // Move para a próxima entrada na lista encadeada
            file.seek(listPointer);
            listPointer = file.readLong();
    
            if (listPointer != 0) {
                System.out.print(" -> ");
            }
        }
    
        // Verifica se mais de um elemento foi inserido no hash index
        if (contador == 0) {
            System.out.print("Nenhum elemento inserido");
        }
    }

    private void imprimirMusica(long musicPointer) throws IOException {
        file.seek(musicPointer + 8); //pula o ID
        long id = file.readLong();
        String titulo = file.readUTF();
        String artista = file.readUTF();
        String estilo = file.readUTF();

        System.out.print("[ID > " + id + ", Titulo > " + titulo + ", Artista > " + artista + ", Estilo > " + estilo + "]");
    }

}
