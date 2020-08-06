package si.rozna.ping.ui.drawer.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> test;

    public LiveData<String> getTest(){
        if(test == null) {
            test = new MutableLiveData<String>();
            loadTest();
        }
        return test;
    }

    private void loadTest() {
        // Asynchronous operations here...

        test.setValue("Test positive");
    }

}
