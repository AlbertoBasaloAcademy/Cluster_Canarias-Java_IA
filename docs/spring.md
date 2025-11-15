# Instrucciones rápidas: instalar, compilar y ejecutar

Breve, directo. Asume JDK instalado y conexión a Internet.

Requisitos
- JDK (el POM usa java 21). Verifica:

```bash
java -version
```

Configurar clave (variable usada en `application.properties` -> ${SPRING_1})
- Bash (WSL / Git Bash / Linux / macOS):

```bash
export SPRING_1="sk-REEMPLAZA_CON_TU_API_KEY"
```

- PowerShell:

```powershell
$env:SPRING_1 = "sk-REEMPLAZA_CON_TU_API_KEY"
```

- Windows CMD:

```cmd
set SPRING_1=sk-REEMPLAZA_CON_TU_API_KEY
```

Compilar (usa el wrapper incluido)

```bash
./mvnw -DskipTests package
```

(Windows cmd: `mvnw.cmd -DskipTests package`)

Ejecutar
- Con Maven (dev loop):

```bash
./mvnw spring-boot:run
```

- Con JAR generado:

```bash
java -jar target/*.jar
```

Probar endpoint (por defecto puerto 8080)

```bash
curl "http://localhost:8080/ama?prompt=hola"
```

Notas cortas
- Puerto por defecto: 8080 (modificar en `src/main/resources/application.properties`).
- Clase principal: `academy.aicode.spring_ai.SpringAiApplication` (puedes ejecutar desde IDE).
- Usa el wrapper `./mvnw` para evitar instalar Maven globalmente.
- Si hay errores de dependencia, limpia caché: `./mvnw clean package -U`.

Fin.
