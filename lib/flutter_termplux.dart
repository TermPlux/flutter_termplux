
import 'flutter_termplux_platform_interface.dart';

class FlutterTermPlux {
  Future<String?> getPlatformVersion() {
    return FlutterTermPluxPlatform.instance.getPlatformVersion();
  }
}
