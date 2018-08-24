package illiyin.mhandharbeni.sejarah.tools;

import illiyin.mhandharbeni.sejarah.model.KategoriModel;
import illiyin.mhandharbeni.sejarah.model.LokasiModel;

public interface ClickListener {
    void onKategoriClick(KategoriModel kategoriModel);
    void onDetailClick(LokasiModel lokasiModel);
}
