package test.andy.tubeswisata.model;

import java.util.List;

public class wisata {
    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private boolean status;
    private List<Data> data;


    public boolean isStatus() {
        return status;
    }


    public List<Data> getData() {
        return data;
    }



    public class Data{
        private String id="";
        private String nama="";
        private String lnglat="";
        private String deskripsi="";
        private String foto="";
        private String kategori="";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getLnglat() {
            return lnglat;
        }

        public void setLnglat(String lnglat) {
            this.lnglat = lnglat;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(String deskripsi) {
            this.deskripsi = deskripsi;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String getKategori() {
            return kategori;
        }

        public void setKategori(String kategori) {
            this.kategori = kategori;
        }






    }


    public wisata() {
    }


}
