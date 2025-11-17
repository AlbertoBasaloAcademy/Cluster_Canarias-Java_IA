# 🎯 Práctica: Sistema de Validación y Manejo de Errores

## 📋 Descripción

Como parte del curso de Java en Canarias, implementaremos un sistema robusto de validación de entrada y manejo de errores para el convertidor de unidades astronómicas. Esta práctica se enfoca en las mejores prácticas de validación, manejo de excepciones y respuestas de error en Spring Boot.

## 🎓 Objetivos de Aprendizaje

Los estudiantes aprenderán a:
- Implementar validaciones personalizadas en Spring Boot
- Crear un sistema centralizado de manejo de excepciones
- Diseñar respuestas de error consistentes y útiles
- Aplicar principios de clean code en validación de datos
- Manejar casos edge y errores de overflow

## 📚 Requisitos Previos

- Conocimientos básicos de Spring Boot
- Familiaridad con anotaciones de validación Java
- Comprensión de HTTP status codes
- Experiencia con manejo de excepciones en Java

## 🚀 Tareas a Realizar

### Fase 1: Validaciones Básicas (30 min)
- [ ] Crear `InputValidator` para validar valores numéricos
- [ ] Implementar validación de rangos científicamente válidos
- [ ] Añadir validación de unidades soportadas

### Fase 2: Manejo de Excepciones (45 min)
- [ ] Crear `ValidationException` personalizada
- [ ] Implementar `GlobalExceptionHandler` con `@ControllerAdvice`
- [ ] Diseñar estructura estándar de `ErrorResponse`

### Fase 3: Casos Edge (30 min)
- [ ] Manejar valores de overflow/underflow
- [ ] Validar entrada de múltiples errores simultáneos
- [ ] Implementar sanitización de entrada

### Fase 4: Testing (45 min)
- [ ] Crear tests unitarios para validadores
- [ ] Tests de integración para manejo de errores
- [ ] Tests de API para respuestas de error

## 📖 Especificación Técnica

Ver documentación completa en: [`003-input_validation_and_error_handling.spec.md`](/docs/specs/003-input_validation_and_error_handling.spec.md)

## ✅ Criterios de Aceptación

1. **Validación Numérica**: Sistema debe rechazar valores negativos o fuera de rango
2. **Unidades Válidas**: Error descriptivo para unidades no soportadas
3. **Respuestas Consistentes**: Todas las respuestas de error siguen formato estándar
4. **Manejo de Overflow**: Gestión elegante de valores extremos
5. **Mensajes Claros**: Errores comprensibles para estudiantes

## 🔧 Recursos Técnicos

### Dependencias Necesarias
- Spring Boot Validation Starter
- Jakarta Bean Validation
- Spring Boot Test

### Archivos a Modificar/Crear
- `src/main/java/academy/aicode/spring_ai/validation/InputValidator.java`
- `src/main/java/academy/aicode/spring_ai/exception/GlobalExceptionHandler.java`
- `src/main/java/academy/aicode/spring_ai/exception/ValidationException.java`
- `src/test/java/academy/aicode/spring_ai/validation/` (tests)

## 💡 Consejos para Estudiantes

1. **Empezar Simple**: Comenzar con validaciones básicas antes de casos complejos
2. **Testing First**: Escribir tests antes de implementar para clarificar requisitos
3. **Mensajes Útiles**: Enfocar en mensajes que ayuden al usuario a corregir errores
4. **Clean Code**: Aplicar principios de código limpio y responsabilidad única

## 🏆 Bonus Points

- [ ] Implementar validación asíncrona para casos complejos
- [ ] Añadir métricas de errores con Micrometer
- [ ] Crear documentación automática de errores con OpenAPI
- [ ] Implementar rate limiting para prevenir abuso

## 📝 Entregables

1. Código fuente implementado y testeado
2. Tests unitarios con cobertura > 80%
3. Documentación de API actualizada
4. Demo funcionando con casos de prueba

## ⏰ Tiempo Estimado

**Total: 2.5 horas**
- Implementación: 1.5 horas
- Testing: 45 minutos  
- Documentación: 15 minutos

---

**Instructor**: @AlbertoBasaloAcademy  
**Etiquetas**: `java`, `spring-boot`, `validation`, `error-handling`, `practica`, `canarias`  
**Dificultad**: Intermedio  
**Duración**: 2.5 horas