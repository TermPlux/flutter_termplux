import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_termplux_method_channel.dart';

abstract class FlutterTermPluxPlatform extends PlatformInterface {
  FlutterTermPluxPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterTermPluxPlatform _instance = MethodChannelFlutterTermPlux();

  static FlutterTermPluxPlatform get instance => _instance;

  static set instance(FlutterTermPluxPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
