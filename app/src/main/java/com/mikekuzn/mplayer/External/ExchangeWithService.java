package com.mikekuzn.mplayer.External;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mikekuzn.mplayer.mPlaerAidlInterface;

import java.util.List;

public class ExchangeWithService implements ExchangeInter {
    private mPlaerAidlInterface aidlSumService;

    public ExchangeWithService(Context context) {
        Log.i("MikeKuzn", "ExchangeWithService Constructor. Start connecting");
        Intent intent = new Intent("MikeKuzn.players.service");
        Intent updatedIntent = createExplicitIntent(context, intent);
        if (updatedIntent != null) {
            context.bindService(updatedIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("MikeKuzn", "onServiceConnected");
            // приводим IBinder к нужному нам типу через Stub реализацию интерфейса
            aidlSumService = mPlaerAidlInterface.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlSumService = null;
        }
    };

    private Intent createExplicitIntent(Context context, Intent intent) {
        // Получить все службы, которые могут соответствовать указанному Intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(intent, 0);

        // Список найденных служб по интенту должен содержать лишь 1 элемент
        if (resolveInfo == null || resolveInfo.size() != 1) {
            // иначе служба на "приложении-сервере" не запущена и мы должны вернуть null
            return null;
        }
        // Получаем информацию о компоненте и создаем ComponentName для Intent
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Повторно используем старый интент
        Intent explicitIntent = new Intent(intent);
        // явно задаем компонент для обработки Intent
        explicitIntent.setComponent(component);

        return explicitIntent;
    }



    @Override
    public void transmitCommand(command id, int numSong) {
        if (aidlSumService != null) {
            try {
                aidlSumService.setCommand(id.ordinal(),numSong);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean transmitList(List<String> pathList) {
        // Transmit songs(path) list
        if (!pathList.isEmpty() && aidlSumService != null) {
            try {
                // Transmit path songs list to player service
                aidlSumService.setList(pathList);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int[] getPlayerState() {
        if (aidlSumService != null) {
            try {
                return aidlSumService.getPlayerState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return new int[0];
    }
}
