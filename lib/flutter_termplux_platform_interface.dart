import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_termplux_method_channel.dart';

abstract class FlutterTermPluxPlatform extends PlatformInterface {
  /// Constructs a FlutterTermpluxPlatform.
  FlutterTermPluxPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterTermPluxPlatform _instance = MethodChannelFlutterTermPlux();

  /// The default instance of [FlutterTermpluxPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterTermplux].
  static FlutterTermPluxPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterTermpluxPlatform] when
  /// they register themselves.
  static set instance(FlutterTermPluxPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
