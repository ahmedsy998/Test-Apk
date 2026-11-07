import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.syriatel.mobile',
  appName: 'SyriaTel',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  },
  android: {
    path: 'android',
    allowMixedContent: false,
    captureInput: true,
    webContentsDebuggingEnabled: true
  }
};

export default config;
