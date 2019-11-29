package com.example.enigma.Interfaces;

public interface WorkingActivityBottomSheets {
    void OpenUserNameBottomSheet();
    void CloseUserNameBottomSheet();
    void OpenHintBottomSheet();
    void CloseHintBottomSheet();
    void OpenLogoutBottomSheet();
    void CloseLogoutBottomSheet();
    void setChecked(int i);
    void snackBarInternetShow();
    void snackBarInternetDismiss();
}
