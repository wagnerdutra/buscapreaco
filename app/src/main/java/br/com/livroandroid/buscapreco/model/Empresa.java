package br.com.livroandroid.buscapreco.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wagner on 15/02/2016.
 */
public class Empresa implements Parcelable {

    private long id;
    private String nome;
    private String cnpj;
    private String rua;
    private int numero;
    public String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String tel;
    private String email;
    private String tipo;
    private String horaInicio;
    private String horaFim;
    private String urlFoto;
    private boolean isSelected;

    public Empresa() {
    }

    protected Empresa(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        cnpj = in.readString();
        rua = in.readString();
        numero = in.readInt();
        bairro = in.readString();
        cidade = in.readString();
        estado = in.readString();
        cep = in.readString();
        tel = in.readString();
        email = in.readString();
        tipo = in.readString();
        horaInicio = in.readString();
        horaFim = in.readString();
        urlFoto = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(cnpj);
        dest.writeString(rua);
        dest.writeInt(numero);
        dest.writeString(bairro);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(cep);
        dest.writeString(tel);
        dest.writeString(email);
        dest.writeString(tipo);
        dest.writeString(horaInicio);
        dest.writeString(horaFim);
        dest.writeString(urlFoto);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Empresa.Creator<Empresa> CREATOR = new Creator<Empresa>() {
        @Override
        public Empresa createFromParcel(Parcel in) {
            return new Empresa(in);
        }

        @Override
        public Empresa[] newArray(int size) {
            return new Empresa[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }



    /*public static List<Empresa> getEmpresas(){
        List<Empresa> empresas = new ArrayList<>();
        empresas.add(new Empresa((long)1,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)2,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)3,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)4,"Ouro Verde","Campo Belo",false));
        empresas.add(new Empresa((long)6,"Mozart","Campo Belo",false));
        empresas.add(new Empresa((long)7,"Alice","Campo Belo",false));
        empresas.add(new Empresa((long)8,"MotoTaxi","Campo Belo",false));
        empresas.add(new Empresa((long)9,"Datacamp","Campo Belo",false));
        empresas.add(new Empresa((long)10,"Dragamaia","Campo Belo",false));
        empresas.add(new Empresa((long)11,"SantaBranca","Campo Belo",false));
        empresas.add(new Empresa((long)12,"Ouro Verde","Campo Belo",false));
        empresas.add(new Empresa((long)13,"Mozart","Campo Belo",false));
        empresas.add(new Empresa((long)14,"Alice","Campo Belo",false));
        empresas.add(new Empresa((long)15,"MotoTaxi","Campo Belo",false));
        empresas.add(new Empresa((long)16,"Datacamp","Campo Belo",false));
        empresas.add(new Empresa((long)17,"Dragamaia","Campo Belo",false));
        empresas.add(new Empresa((long)18,"SantaBranca","Campo Belo",false));
        empresas.add(new Empresa((long)19,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)20,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)21,"ABC","Campo Belo",false));
        empresas.add(new Empresa((long)22,"Ouro Verde","Campo Belo",false));
        empresas.add(new Empresa((long)23,"Mozart","Campo Belo",false));
        empresas.add(new Empresa((long)24,"Alice","Campo Belo",false));
        empresas.add(new Empresa((long)25,"MotoTaxi","Campo Belo",false));

        return empresas;
    }*/
}