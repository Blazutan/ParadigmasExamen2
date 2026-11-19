# Sistema de registro de exámenes de sangre

Aplicación de escritorio desarrollada en Java con JavaFX para registrar exámenes
de sangre, consultar los registros y almacenarlos mediante serialización JSON.

El proyecto aplica Arquitectura Hexagonal y los patrones Factory Method y
Strategy para separar el dominio, los casos de uso y la infraestructura.

## Funcionalidades

- Registrar exámenes con código, paciente, costo, tipo de sangre y factor RH.
- Impedir códigos duplicados, sin distinguir mayúsculas y minúsculas.
- Listar todos los exámenes registrados.
- Limpiar los campos del formulario.
- Serializar los exámenes en `examenes-sangre.json`.
- Recuperar los exámenes desde el archivo JSON.
- Validar datos obligatorios y valores incorrectos.

## Tecnologías

- Java 11
- JavaFX 13
- Maven
- JUnit 5
- SonarQube

## Requisitos

Instale las siguientes herramientas:

- JDK 11 o superior compatible con JavaFX 13.
- Apache Maven 3.6 o superior.
- Git.
- SonarQube, únicamente si desea ejecutar el análisis de calidad local.

Verifique las instalaciones desde una terminal:

```powershell
java -version
mvn -version
git --version
```

## Instalación

Clone el repositorio:

```powershell
git clone https://github.com/Blazutan/ParadigmasExamen2.git
cd ParadigmasExamen2
```

Si ya dispone del proyecto, abra una terminal dentro de la carpeta:

```powershell
cd Examen2
```

Descargue las dependencias y compile el proyecto:

```powershell
mvn clean compile
```

## Configuración

No se requiere configurar una base de datos ni variables de entorno.

Los exámenes se mantienen temporalmente en memoria y pueden persistirse en el
archivo JSON `examenes-sangre.json`. Este archivo se crea en el directorio desde
el cual se ejecuta la aplicación.

Las rutas principales son:

```text
Examen2/
├── pom.xml
├── src/main/java/
├── src/main/resources/
└── src/test/java/
```

La configuración de Maven y las dependencias se encuentra en
[Examen2/pom.xml](Examen2/pom.xml).

## Ejecutar la aplicación

Desde la carpeta `Examen2`, ejecute:

```powershell
mvn clean javafx:run
```

También puede compilar y ejecutar el JAR generado:

```powershell
mvn clean package
java -jar target/Examen2-0.0.1-SNAPSHOT.jar
```

El JAR requiere que las dependencias de JavaFX estén disponibles en el entorno
de ejecución. Para iniciar desde Maven se recomienda `mvn javafx:run`.

## Pruebas unitarias

Las pruebas utilizan JUnit 5 y están ubicadas en `Examen2/src/test/java`.

Ejecute todas las pruebas:

```powershell
mvn test
```

Ejecute una clase de pruebas específica:

```powershell
mvn -Dtest=SerializadorJsonTest test
```

Los resultados aparecen en `Examen2/target/surefire-reports`.

## Análisis con SonarQube

Configure SonarQube mediante las propiedades de Maven o variables de entorno.

Ejemplo de ejecución con parámetros:

```powershell
mvn clean verify sonar:sonar `
  -Dsonar.projectKey=ParadigmasExamen2 `
  -Dsonar.host.url=http://localhost:9000 `
  -Dsonar.login=SU_TOKEN
```


## Arquitectura del proyecto

El proyecto organiza las responsabilidades de la siguiente manera:

- `aplication.domain.model`: entidades y reglas básicas del dominio.
- `aplication.port.in`: interfaces de los casos de uso.
- `aplication.port.out`: puertos de repositorio y serialización.
- `aplication.service`: implementación de los casos de uso.
- `aplication.facade`: interfaz simplificada para la interfaz gráfica.
- `infrastructure.persistence`: repositorio en memoria y serializadores.
- `infrastructure.factory`: construcción de dependencias predeterminadas.
- `infrastructure.ui`: controlador JavaFX.
- `vista`: punto de entrada de la aplicación.

Las clases principales incluyen documentación JavaDoc.

## Solución de problemas

### Maven no reconoce el JDK

Compruebe que `JAVA_HOME` apunta al JDK 11 y que su directorio `bin` está en
`PATH`:

```powershell
$env:JAVA_HOME="C:\Ruta\Al\JDK-11"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
```

### JavaFX no inicia

Ejecute primero:

```powershell
cd Examen2
mvn clean javafx:run
```

Evite ejecutar manualmente clases JavaFX si no se han agregado los módulos y
dependencias al classpath.

