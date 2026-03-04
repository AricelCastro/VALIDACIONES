<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Swal from 'sweetalert2'

const alumnos = ref([])

const nuevoAlumno = ref({
  nombre: '',
  apellido: '',
  carrera: '',
  telefono: '',
  imagenURL: ''
})

const editado = ref(false)
const errores = ref({})

// 🔥 Cambia el puerto si tu backend está en 8081
const API = 'http://localhost:8080/alumnos'

// =====================
// Cargar alumnos
// =====================
const cargarAlumnos = async () => {
  try {
    const response = await axios.get(`${API}/traer-alumnos`)
    alumnos.value = response.data
  } catch (error) {
    console.error(error)
  }
}

// =====================
// Validación
// =====================
const validarFormulario = () => {
  errores.value = {}

  if (!nuevoAlumno.value.nombre.trim()) {
    errores.value.nombre = "El campo es obligatorio"
  }

  if (!nuevoAlumno.value.apellido.trim()) {
    errores.value.apellido = "El campo es obligatorio"
  }

  if (!nuevoAlumno.value.carrera.trim()) {
    errores.value.carrera = "El campo es obligatoria"
  }

  if (!/^\d{10}$/.test(nuevoAlumno.value.telefono)) {
    errores.value.telefono = "El teléfono debe tener 10 dígitos"
  }

  if (
    nuevoAlumno.value.imagenURL &&
    !/^https?:\/\/.+\..+/.test(nuevoAlumno.value.imagenURL)
  ) {
    errores.value.imagenURL = "La URL no es válida"
  }

  return Object.keys(errores.value).length === 0
}

// =====================
// Agregar o actualizar
// =====================
const agregarAlumno = async () => {

  if (!validarFormulario()) {
    Swal.fire({
      icon: 'error',
      title: 'Formulario inválido',
      text: 'Por favor corrige los errores'
    })
    return
  }

  try {
    if (editado.value) {
      await axios.put(
        `${API}/editar-alumnos/${nuevoAlumno.value.id}`,
        nuevoAlumno.value
      )
      editado.value = false
    } else {
      await axios.post(
        `${API}/insertar-alumnos`,
        nuevoAlumno.value
      )

      Swal.fire({
        icon: 'success',
        title: 'Alumno agregado correctamente',
        showConfirmButton: false,
        timer: 1500
      })
    }

    await cargarAlumnos()

    nuevoAlumno.value = {
      nombre: '',
      apellido: '',
      carrera: '',
      telefono: '',
      imagenURL: ''
    }

    errores.value = {}

  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'Ocurrió un problema al guardar'
    })
  }
}

// =====================
// Editar
// =====================
const editarAlumnos = (alumno) => {
  Object.assign(nuevoAlumno.value, alumno)
  editado.value = true
}

// =====================
// Eliminar
// =====================
const eliminarAlumno = async (id) => {
  Swal.fire({
    title: '¿Estás seguro?',
    text: "No podrás revertir esto",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, eliminar'
  }).then(async (result) => {
    if (result.isConfirmed) {
      await eliminarAlumnoPorId(id)
    }
  })
}

const eliminarAlumnoPorId = async (id) => {
  try {
    await axios.delete(`${API}/eliminar-alumnos/${id}`)
    await cargarAlumnos()

    Swal.fire(
      'Eliminado!',
      'El alumno ha sido eliminado.',
      'success'
    )
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No se pudo eliminar el alumno.'
    })
  }
}

onMounted(cargarAlumnos)
</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Alumnos</h2>

          <form @submit.prevent="agregarAlumno">
            <div class="row">

              <!-- Nombre -->
              <div class="col-md-6 mb-3">
                <label class="form-label">Nombre</label>
                <input type="text"
                       class="form-control"
                       :class="{ 'is-invalid': errores.nombre }"
                       v-model="nuevoAlumno.nombre">
                <small class="text-danger" v-if="errores.nombre">
                  {{ errores.nombre }}
                </small>
              </div>

              <!-- Apellido -->
              <div class="col-md-6 mb-3">
                <label class="form-label">Apellidos</label>
                <input type="text"
                       class="form-control"
                       :class="{ 'is-invalid': errores.apellido }"
                       v-model="nuevoAlumno.apellido">
                <small class="text-danger" v-if="errores.apellido">
                  {{ errores.apellido }}
                </small>
              </div>

              <!-- Carrera -->
              <div class="col-md-6 mb-3">
                <label class="form-label">Carrera</label>
                <input type="text"
                       class="form-control"
                       :class="{ 'is-invalid': errores.carrera }"
                       v-model="nuevoAlumno.carrera">
                <small class="text-danger" v-if="errores.carrera">
                  {{ errores.carrera }}
                </small>
              </div>

              <!-- Teléfono -->
              <div class="col-md-6 mb-3">
                <label class="form-label">Teléfono</label>
                <input type="text"
                       class="form-control"
                       :class="{ 'is-invalid': errores.telefono }"
                       v-model="nuevoAlumno.telefono"
                       inputmode="numeric">
                <small class="text-danger" v-if="errores.telefono">
                  {{ errores.telefono }}
                </small>
              </div>

              <!-- Imagen URL -->
              <div class="col-md-6 mb-3">
                <label class="form-label">Imagen URL</label>
                <input type="text"
                       class="form-control"
                       :class="{ 'is-invalid': errores.imagenURL }"
                       v-model="nuevoAlumno.imagenURL">
                <small class="text-danger" v-if="errores.imagenURL">
                  {{ errores.imagenURL }}
                </small>
              </div>

            </div>

            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Alumno' : 'Agregar Alumno' }}
            </button>
          </form>
        </div>
      </div>

      <!-- Tabla -->
      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h5 class="card-title mb-3">Lista de Alumnos</h5>
            <table class="table table-hover align-middle">
              <thead class="table-light">
                <tr>
                  <th>Id</th>
                  <th>Nombre</th>
                  <th>Apellidos</th>
                  <th>Carrera</th>
                  <th>Teléfono</th>
                  <th>Imagen</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="alumno in alumnos" :key="alumno.id">
                  <td>{{ alumno.id }}</td>
                  <td>{{ alumno.nombre }}</td>
                  <td>{{ alumno.apellido }}</td>
                  <td>{{ alumno.carrera }}</td>
                  <td>{{ alumno.telefono }}</td>
                  <td>
                    <img v-if="alumno.imagenURL"
                         :src="alumno.imagenURL"
                         width="50">
                  </td>
                  <td>
                    <button @click="eliminarAlumno(alumno.id)"
                            class="btn btn-danger mx-2">
                      🗑
                    </button>
                    <button @click="editarAlumnos(alumno)"
                            class="btn btn-warning">
                      ✏
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped></style>