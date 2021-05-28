package com.akash.cp.vtu.vtupartb_8;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class EMIService extends Service {
    public EMIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return aidlService;
    }
    AIDLService.Stub aidlService=new AIDLService.Stub() {
        @Override
        public double CarEMICalculation(double principal, double downpayment, double rate, int term) throws RemoteException {
            double emi;
            double totalPrincipal=principal-downpayment;
            rate = rate / (12 * 100); // one month interest
            term = term * 12; //month period
            emi = (totalPrincipal * rate *  Math.pow(1 + rate, term)) / (Math.pow(1 + rate, term) - 1);
            return emi;
        }
    };
}