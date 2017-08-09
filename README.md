原理：

1.静默安装、卸载是利用pm install命令来安装apk,pm uninstall 来卸载apk,但都需要root权限;

2.智能安装是利用android系统提供的无障碍服务AccessibilityService,来模拟用户点击,从而自动安装.
