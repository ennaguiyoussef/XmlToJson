# XmlToJson Transformer

A JavaFX-based desktop application for converting XML to JSON and JSON to XML formats with support for both API-based and native implementations.

## 📹 Demo Video

Watch the project demonstration video here:
[XmlToJson Transformer Demo](https://docs.google.com/videos/d/1U85H-qCf2fGYaUDUOvqlZ1hFUiB1NtjCNRO2SD0HNhc/edit?usp=sharing)

## 🚀 Features

- **Bidirectional Conversion**: Convert XML to JSON and JSON to XML
- **Dual Implementation Modes**:
  - **API Mode**: Uses proven libraries for reliable conversion
  - **Native Mode**: Custom implementation using core Java APIs
- **User-Friendly GUI**: Built with JavaFX for an intuitive interface
- **File Operations**: Open and save XML/JSON files
- **Live Conversion**: Real-time conversion with error handling
- **Swap Functionality**: Quick swap between input and output

## 🛠️ Technologies Used

### Core Technologies

- **Java 24**: The latest version of Java with modern language features
- **JavaFX 21.0.6**: For building the graphical user interface
  - `javafx-controls`: UI components
  - `javafx-fxml`: FXML-based UI design

### Build & Project Management

- **Maven**: Project build automation and dependency management
- **Maven Compiler Plugin 3.13.0**: For compiling Java source code
- **JavaFX Maven Plugin 0.0.8**: For running and packaging JavaFX applications

### XML/JSON Conversion Libraries

#### API-Based Conversion
- **org.json 20240303**: Lightweight JSON library for XML-JSON conversion
  - Provides `XML.toJSONObject()` for easy XML to JSON conversion
- **Jackson XML/JSON 2.16.1**:
  - `jackson-dataformat-xml`: XML data format support
  - `jackson-databind`: JSON data binding capabilities

#### Native Implementation
- **Java DOM Parser** (`javax.xml.parsers`): For parsing XML documents
- **Java SAX** (`org.xml.sax`): For efficient XML processing
- **Custom JSON Generation**: Hand-crafted JSON builder with proper escaping and formatting

### UI Enhancement Libraries

- **ControlsFX 11.2.1**: Additional UI controls and dialogs
- **Ikonli JavaFX 12.3.1**: Icon packs for enhanced visual elements

### Testing Framework

- **JUnit Jupiter 5.12.1**: Modern testing framework for unit tests
  - `junit-jupiter-api`: API for writing tests
  - `junit-jupiter-engine`: Engine for running tests

## 📦 Project Structure

```
XmlToJson/
├── src/main/java/com/example/transformers/
│   ├── HelloApplication.java          # Main application entry point
│   ├── HelloController.java           # UI controller with event handlers
│   ├── Launcher.java                  # Application launcher
│   └── converter/
│       ├── api/                       # API-based converters
│       │   ├── XmlToJsonApi.java
│       │   └── JsonToXmlApi.java
│       └── natif/                     # Native implementations
│           ├── XmlToJsonNatif.java
│           └── JsonToXmlNatif.java
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

## 🔧 Building and Running

### Prerequisites
- Java 24 or higher
- Maven 3.6 or higher

### Build the Project
```bash
mvn clean compile
```

### Run the Application
```bash
mvn javafx:run
```

### Run Tests
```bash
mvn test
```

## 💡 How It Works

### API Mode
The API mode leverages well-tested libraries:
- **XML to JSON**: Uses `org.json.XML` to parse XML and convert to JSON
- **JSON to XML**: Uses Jackson's XML data format for reliable conversion

### Native Mode
The native implementation uses core Java APIs:
- **XML Parsing**: DOM parser for reading XML structure
- **Custom JSON Builder**: Manually constructs JSON with:
  - Proper escaping of special characters
  - Handling of XML attributes
  - Support for nested elements and arrays
  - Text content preservation

## 🎯 Key Implementation Details

- **Mode Selection**: RadioMenuItem toggle between API and Native modes
- **File Chooser**: Supports .xml and .json file extensions
- **Error Handling**: Comprehensive try-catch blocks with user feedback
- **Text Escaping**: Custom JSON escaping for native implementation
- **Array Detection**: Automatically converts multiple XML elements with same name to JSON arrays

## 📝 License

This project is part of a learning exercise demonstrating different approaches to XML/JSON transformation in Java.
