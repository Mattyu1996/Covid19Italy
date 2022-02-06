package it.univaq.disim.mwt.covid19italy;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GasPlatformViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Provincia>> platforms = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Provincia>> nearPlatforms = new MutableLiveData<>();

    public MutableLiveData<Provincia> getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(MutableLiveData<Provincia> currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    private MutableLiveData<Provincia> currentPlatform = new MutableLiveData<>();

    public void setNearPlatforms(ArrayList<Provincia> piattaformeVicine){
        this.nearPlatforms.setValue(piattaformeVicine);
    }

    public void setPlatforms(ArrayList<Provincia> piattaforme) {
        this.platforms.setValue(piattaforme);
    }

    public MutableLiveData<ArrayList<Provincia>> getPlatforms() {
        return this.platforms;
    }

    public MutableLiveData<ArrayList<Provincia>> getNearPlatforms(){
        return this.nearPlatforms;
    }
}
