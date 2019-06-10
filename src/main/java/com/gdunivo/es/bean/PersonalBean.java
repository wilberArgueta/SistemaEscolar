package com.gdunivo.es.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


import org.primefaces.event.SelectEvent;

import com.gdunivo.es.model.Personal;
import com.gdunivo.es.model.RolPersonal;
import com.gdunivo.es.model.Roles;
import com.gdunivo.es.repository.PersonalRepository;
import com.gdunivo.es.repository.RepositoryFactory;
import com.gdunivo.es.repository.RolPersonalRepository;

@ManagedBean(name = "personal")
@SessionScoped
public class PersonalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Personal personal = new Personal();
	private Personal personalSelect = new Personal();
	private Date fecha;
	private List<Personal> listaPersonal = new ArrayList<>();
	private boolean seleccionado = false;
	private RolPersonal rPersonal = new RolPersonal();
	private Roles rol = new Roles();
	private Roles admin = new Roles(1, "Admin");
	private Roles perso = new Roles(2, "Personal");

	private PersonalRepository pPersonal = (PersonalRepository) RepositoryFactory.getRepository("Personal");
	private RolPersonalRepository pRPersonal = (RolPersonalRepository) RepositoryFactory.getRepository("RolPersonal");

	public PersonalBean() {
		this.actualizarTabla();
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Personal getPersonalSelect() {
		return personalSelect;
	}

	public void setPersonalSelect(Personal personalSelect) {
		this.personalSelect = personalSelect;
	}

	public List<Personal> getListaPersonal() {
		return listaPersonal;
	}

	public void setListaPersonal(List<Personal> listaPersonal) {
		this.listaPersonal = listaPersonal;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public RolPersonal getrPersonal() {
		return rPersonal;
	}

	public void setrPersonal(RolPersonal rPersonal) {
		this.rPersonal = rPersonal;
	}

	public Roles getAdmin() {
		return admin;
	}

	public void setAdmin(Roles admin) {
		this.admin = admin;
	}

	public Roles getPerso() {
		return perso;
	}

	public void setPerso(Roles perso) {
		this.perso = perso;
	}

	public Roles getRol() {
		return rol;
	}

	public void setRol(Roles rol) {
		this.rol = rol;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public LocalDate convertDate(Date e) {
		return new java.sql.Date(e.getTime()).toLocalDate();
	}

	public Date convertToDateViaInstant(LocalDate dateToConvert) {

		return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public void registrar() {
		System.out.println("Entro");
		this.personal.setFechaNacimiento(convertDate(this.fecha));
		this.personal = this.pPersonal.guardar(this.personal);
		System.out.println(this.personal);
		this.rPersonal.setPersonal(this.personal);
		this.rPersonal.setRol(this.rol);
		this.pRPersonal.guardar(this.rPersonal);
		this.limpiarFormulario();
		this.actualizarTabla();
	}

	public void actualizarTabla() {
		this.listaPersonal = this.pPersonal.obtenerListado();
	}

	public void limpiarFormulario() {
		this.seleccionado = false;
		this.personal = new Personal();
		this.fecha = null;
		this.rPersonal = new RolPersonal();
	}

	public void seleccionarPersonal(SelectEvent event) {

		this.personalSelect = (Personal) event.getObject();
		this.personal = this.personalSelect;
		this.fecha = this.convertToDateViaInstant(this.personal.getFechaNacimiento());
		this.seleccionado = true;
	}

	public void actualizarPersonal() {
		System.out.println("Actualizar personal");
		this.personal.setFechaNacimiento(convertDate(this.fecha));
		this.personal = this.pPersonal.actualizar(this.personal);
		this.rPersonal.setPersonal(this.personal);
		this.rPersonal.setRol(this.rol);
		this.pRPersonal.actualizar(this.rPersonal);
		this.limpiarFormulario();
		this.actualizarTabla();

	}

	public void eliminarPersonal() {

		this.pPersonal.eliminar(this.personal);
		this.limpiarFormulario();
		this.actualizarTabla();
	}

}
