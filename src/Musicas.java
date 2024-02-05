
public class Musicas {

    private long id;
    private String titulo;
    private String estilo;
    private String artista;


    public Musicas(long id, String titulo, String estilo, String artista) {
        this.id = id;
        this.titulo = titulo;
        this.estilo = estilo;
        this.artista = artista;
    }

    public Musicas(){
    }


    public long getId() {
        return id;
    }


    public void setId(long i) {
        this.id = i;
    }


    public String getTitulo() {
        return titulo;
    }


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getEstilo() {
        return estilo;
    }


    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }


    public String getArtista() {
        return artista;
    }


    public void setArtista(String artista) {
        this.artista = artista;
    }

    @Override
    public String toString() {
        return "\nID > " + getId() + 
        "\nTitulo > " + getTitulo() + 
        "\nArtista > " + getArtista() + 
        "\nEstilo > " + getEstilo();
    }








}