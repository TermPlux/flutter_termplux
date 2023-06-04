import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_termplux/flutter_termplux.dart';
import 'package:flutter_termplux/flutter_termplux_platform_interface.dart';
import 'package:flutter_termplux/flutter_termplux_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterTermpluxPlatform
    with MockPlatformInterfaceMixin
    implements FlutterTermPluxPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterTermPluxPlatform initialPlatform = FlutterTermPluxPlatform.instance;

  test('$MethodChannelFlutterTermPlux is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterTermPlux>());
  });

  test('getPlatformVersion', () async {
    FlutterTermPlux flutterTermpluxPlugin = FlutterTermPlux();
    MockFlutterTermpluxPlatform fakePlatform = MockFlutterTermpluxPlatform();
    FlutterTermPluxPlatform.instance = fakePlatform;

    expect(await flutterTermpluxPlugin.getPlatformVersion(), '42');
  });
}
