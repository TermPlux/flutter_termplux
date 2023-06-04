import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_termplux_platform_interface.dart';

/// An implementation of [FlutterTermpluxPlatform] that uses method channels.
class MethodChannelFlutterTermPlux extends FlutterTermPluxPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_termplux');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
