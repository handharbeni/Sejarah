package illiyin.mhandharbeni.sejarah.model;

/**
 * Created by root on 2/10/18.
 */

public class LokasiModel {
    String nama, alamat, latitude, longitude, star, deskripsi, thumbnail;

    public LokasiModel() {
    }

    public LokasiModel(String nama, String alamat, String latitude, String longitude, String star, String deskripsi, String thumbnail) {
        this.nama = nama;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.star = star;
        this.deskripsi = deskripsi;
        this.thumbnail = thumbnail;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
