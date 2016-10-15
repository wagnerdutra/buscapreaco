package br.com.livroandroid.buscapreco.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wagner on 14/02/2016.
 */
public class Produto implements Parcelable{

    private long id;
    private long idEmpresa;
    private String nome;
    private float precoVista;
    private float precoPromocao;
    private String unidade;
    private String codBarras;
    private String urlFoto;
    private boolean isChecked;
    private float qtd;
    private boolean selected;

    public Produto() {
    }

    protected Produto(Parcel in) {
        id = in.readLong();
        idEmpresa = in.readLong();
        nome = in.readString();
        precoVista = in.readFloat();
        precoPromocao = in.readFloat();
        unidade = in.readString();
        codBarras = in.readString();
        urlFoto = in.readString();
        isChecked = in.readByte() != 0;
        qtd = in.readFloat();
        selected = in.readByte() != 0;
    }

    public static final Produto.Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(idEmpresa);
        parcel.writeString(nome);
        parcel.writeFloat(precoVista);
        parcel.writeFloat(precoPromocao);
        parcel.writeString(unidade);
        parcel.writeString(codBarras);
        parcel.writeString(urlFoto);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeFloat(qtd);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPrecoVista() {
        return precoVista;
    }

    public void setPrecoVista(float precoVista) {
        this.precoVista = precoVista;
    }

    public float getPrecoPromocao() {
        return precoPromocao;
    }

    public void setPrecoPromocao(float precoPromocao) {
        this.precoPromocao = precoPromocao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public float getQtd() {
        return qtd;
    }

    public void setQtd(float qtd) {
        this.qtd = qtd;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static Creator<Produto> getCREATOR() {
        return CREATOR;
    }

    /*public static List<Produto> getProdutos(Empresa empresa){
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto((long)1,empresa,"Bolacha Passatempo",(float) 2.75,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Carne de Vaca", 5, 0,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Desodorante",(float) 7.99,0,"UN","4005900122186"));
        produtos.add(new Produto((long)1,empresa,"Macarrão", 3, 0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Alface", 2,0,"KG","7891234123459"));
        produtos.add(new Produto((long)1,empresa,"Saco Cimento", 23,19,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Livro Android", 58,0,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Tinta Cabelo",7,5,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Bolacha Passatempo",(float) 2.75,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Carne de Vaca", 5,4,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Livro Eva",(float) 10,(float) 8.99,"UN","9788580414615"));
        produtos.add(new Produto((long)1,empresa,"Macarrão", 3,(float) 2.75,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Alface", 2,0,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Saco Cimento", 23,19,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Livro Android", 58,0,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Tinta Cabelo",7,5,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Bolacha Passatempo",(float) 2.75,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Carne de Vaca", 5,4,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Chocolate",(float) 0.5,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Macarrão", 3,(float) 2.75,"UN","7898357417892"));
        produtos.add(new Produto((long)1,empresa,"Alface", 2,0,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Caneta", (float)0.75,0,"Un","7896572000547"));
        produtos.add(new Produto((long)1,empresa,"Livro Android", 58,0,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Tinta Cabelo",7,5,"Un","9788575224403"));
        produtos.add(new Produto((long)1,empresa,"Bolacha Passatempo",(float) 2.75,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Carne de Vaca", 5,4,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Doce",(float) 0.5,0,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Macarrão", 3,(float) 2.75,"UN","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Alface", 2,0,"KG","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Saco Cimento", 23,19,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Livro Android", 58,0,"Un","0000000000000"));
        produtos.add(new Produto((long)1,empresa,"Tinta Cabelo",7,5,"Un","0000000000000"));
        return produtos;
    }*/
}