Esta versión une la base funcional original (persistencia real con JPA, usuarios, estaciones, pagos) con la version requerida para este segundo parcial.


### A. Reglas de Negocio

- **Gestión de Vehículos:** La plataforma ofrece dos tipos de vehículos:
  - **Monopátines:** Requieren registro del sistema de amortiguación
  - **Bicicletas Eléctricas:** Requieren registro de capacidad en cm³

- **Gestión de Usuarios:** Dos tipos con beneficios diferenciados:
  - **Usuarios Regulares:** Sin descuento en tarifas
  - **Usuarios Premium:** Descuento fijo del 15% en todos los alquileres

- **Estaciones de Anclaje:** Puntos estratégicos para buscar y dejar vehículos
  - Identificadas por nombre único
  - Contienen información de ubicación (lat/long)
  - Capaces de almacenar múltiples vehículos

- **Procesamiento de Pagos:** 
  - Métodos soportados: Tarjeta de Crédito, Billetera Virtual
  - Descuentos aplicables directamente sobre el total