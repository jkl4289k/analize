//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how Android Studio suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
            dependencies:
            flutter:
            sdk: flutter
            http: ^0.13.4
import 'dart:io';
import 'dart:convert';
import 'package:http/http.dart' as http;

            class FoodLensApiService {
                final String _apiKey = 'YOUR_API_KEY_HERE'; // 여기에 API 키를 넣어주세요.
                final String _apiUrl = 'https://api.foodlens.com/v2/recognize'; // 푸드렌즈 API URL
                   Future<Map<String, dynamic>> recognizeFood(File imageFile) async {
                    try {
                        var request = http. MultipartRequest('POST', Uri.parse(_apiUrl));
                        request.headers['Authorization'] = 'Bearer $_apiKey'; // API 키를 헤더에 추가

                        // 이미지 파일을 멀티파트(multipart) 형태로 요청에 첨부
                        request.files.add(
                                await http.MultipartFile.fromPath('image', imageFile.path),
                                );

                        var response = await request.send();

                        if (response.statusCode == 200) {
                            var responseBody = await response.stream.bytesToString();
                            return json.decode(responseBody);
                        } else {
                            throw Exception('Failed to recognize food: ${response.statusCode}');
                        }
                    } catch (e) {
                        throw Exception('API call failed: $e');
                    }
                }
            }
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';
import 'food_lens_api_service.dart'; // 위에서 만든 서비스 파일

            class FoodRecognitionScreen extends StatefulWidget {
                @override
                _FoodRecognitionScreenState createState() => _FoodRecognitionScreenState();
            }

            class _FoodRecognitionScreenState extends State<FoodRecognitionScreen> {
                File? _image;
                String _resultText = '결과를 기다리는 중...';
                bool _isLoading = false;

                final picker = ImagePicker();
                final apiService = FoodLensApiService();

                Future getImageAndRecognize() async {
                    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

                    if (pickedFile != null) {
                        setState(() {
                            _image = File(pickedFile.path);
                            _isLoading = true;
                        });

                        try {
                            final result = await apiService.recognizeFood(_image!);
                            // API 응답에서 음식 이름 또는 관련 정보 추출
                            if (result.containsKey('food')) {
                                setState(() {
                                    _resultText = '인식된 음식: ${result['food']}';
                                });
                            } else {
                                setState(() {
                                    _resultText = '음식을 인식할 수 없습니다.';
                                });
                            }
                        } catch (e) {
                            setState(() {
                                _resultText = '에러 발생: $e';
                            });
                        } finally {
                            setState(() {
                                _isLoading = false;
                            });
                        }
                    }
                }

                @override
                Widget build(BuildContext context) {
                    return Scaffold(
                            appBar: AppBar(title: Text('푸드렌즈 앱')),
                    body: Center(
                            child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                    if (_image != null)
                        Image.file(_image!, height: 300)
            else
                    Text('이미지를 선택하세요.'),
                            SizedBox(height: 20),
                    _isLoading
                            ? CircularProgressIndicator()
                            : ElevatedButton(
                            onPressed: getImageAndRecognize,
                            child: Text('음식 사진 분석하기'),
                  ),
                    SizedBox(height: 20),
                    Text(_resultText),
          ],
        ),
      ),
    );
                }
            }