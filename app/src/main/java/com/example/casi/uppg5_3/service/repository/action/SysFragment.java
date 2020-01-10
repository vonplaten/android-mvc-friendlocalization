package com.example.casi.uppg5_3.service.repository.action;



import com.example.casi.uppg5_3.service.model.FragmentPicker;

class SysFragment extends SystemAction {
    private FragmentPicker fragmentPicker;

    public SysFragment(FragmentPicker fragmentPicker) {
        super();
        this.fragmentPicker = fragmentPicker;
    }

    @Override
    public void run() {
        repository.getFragmentPickerObservable().postValue(fragmentPicker);

    }

}
