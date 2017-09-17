package com.dotsys.doctorschamber.repository;

import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.util.APIClientService;
import com.dotsys.doctorschamber.util.Global;

/**
 * Created by cio on 8/7/2017.
 */

public class TreatmentRequestRepository {
    APIClientService service;

    public TreatmentRequestRepository(){
        service = new APIClientService();
    }


    public boolean RequestNow(TreatmentRequest treatmentRequest){
        try {
            return service.RequestNow(treatmentRequest);
        }catch (Exception e){
            throw e;
        }
    }

    public void MyPendingRequests(){
        try {
            Global.myPendingRequest = service.MyPendingRequests();
        }catch (Exception e){
            throw e;
        }
    }

    public void MyClosedRequests(){
        try {
            Global.myClosedRequest = service.MyClosedRequests();
        }catch (Exception e){
            throw e;
        }
    }

    public boolean CancelRequests(final TreatmentRequest treatmentRequest){
        try {
            treatmentRequest.SET_chargeStatus("Canceled");
            treatmentRequest.SET_requestStatus("Closed");

            if(service.UpdateRequest(treatmentRequest)){
                int selectedIndex = Global.myPendingRequest.indexOf(treatmentRequest);
                TreatmentRequest selected = Global.myPendingRequest.get(selectedIndex);

                selected.SET_chargeStatus("Canceled");
                selected.SET_requestStatus("Closed");
                return true;
            }
            return false;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean ConfirmRequests(final TreatmentRequest treatmentRequest){
        try {
            treatmentRequest.SET_chargeStatus("Confirmed");

            if(service.UpdateRequest(treatmentRequest)){
                int selectedIndex = Global.myPendingRequest.indexOf(treatmentRequest);
                TreatmentRequest selected = Global.myPendingRequest.get(selectedIndex);

                selected.SET_chargeStatus("Confirmed");
                return true;
            }
            return false;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean DoneRequests(final TreatmentRequest treatmentRequest){
        try {
            treatmentRequest.SET_treatmentStatus("Taken");

            if(service.UpdateRequest(treatmentRequest)){
                int selectedIndex = Global.myPendingRequest.indexOf(treatmentRequest);
                TreatmentRequest selected = Global.myPendingRequest.get(selectedIndex);

                selected.SET_treatmentStatus("Taken");
                selected.SET_treatmentInfo(treatmentRequest.GET_treatmentInfo());
                selected.SET_paymentStatus(treatmentRequest.GET_paymentStatus());
                return true;
            }
            return false;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean CompleteRequests(final TreatmentRequest treatmentRequest){
        try {
            treatmentRequest.SET_requestStatus("Closed");

            if(service.UpdateRequest(treatmentRequest)){
                int selectedIndex = Global.myPendingRequest.indexOf(treatmentRequest);
                TreatmentRequest selected = Global.myPendingRequest.get(selectedIndex);

                selected.SET_requestStatus("Closed");
                return true;
            }
            return false;
        }catch (Exception e){
            throw e;
        }
    }

}
