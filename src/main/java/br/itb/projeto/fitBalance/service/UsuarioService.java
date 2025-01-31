package br.itb.projeto.fitBalance.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.itb.projeto.fitBalance.model.entity.Exercicios;
import br.itb.projeto.fitBalance.model.entity.Usuario;
import br.itb.projeto.fitBalance.model.repository.ArquivoRepository;
import br.itb.projeto.fitBalance.model.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;
	private ArquivoRepository arquivoRepository;
	
	

	public UsuarioService(UsuarioRepository usuarioRepository, ArquivoRepository arquivoRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.arquivoRepository = arquivoRepository;
	}

	public List<Usuario> findAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		return usuarios;
	}

	public Usuario findById(long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isPresent()) {
			Usuario resultado = usuario.get();
			resultado.setFoto(null);
			return resultado;
		}

		return null;
	}

	public Usuario findByEmail(String email) {
		Usuario usuario = usuarioRepository.obterPorEmail(email);
		if (usuario != null) {
			return usuario;
		}
		return null;
	}

	// A CONTA DE USUÁRIO SERÁ CRIADA COM UMA SENHA PADRÃO
	// ELE DEVE ALTERAR NO PRIMEIRO ACESSO
	@Transactional
	public Usuario createNew(Usuario usuario) {
		
		Usuario _usuario = usuarioRepository.obterPorEmail(usuario.getEmail());

		if (_usuario == null) {
			String senha = Base64.getEncoder()
					.encodeToString(usuario.getSenha().getBytes());
			
			usuario.setSenha(senha);
			usuario.setStatusUsuario("ATIVO");
			
			return usuarioRepository.save(usuario);
		}
		return null;
		
	}
	
	// A CONTA DE USUÁRIO SERÁ CRIADA COM SENHA DEFINIDA POR ELE
	@Transactional
	public Usuario create(Usuario usuario) {
		
		String senha = Base64.getEncoder()
			.encodeToString(usuario.getSenha().getBytes());
		
		usuario.setSenha(senha);
		usuario.setStatusUsuario("ATIVO");
		
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public Usuario createTeste(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}
	
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}
	

	@Transactional
	public Usuario signin(String email, String senha) {
	 Usuario usuario = usuarioRepository.obterPorEmail(email);
		
	 if(usuario != null) {
		if (!usuario.getStatusUsuario().equals("INATIVO")) {
			byte[] decodedPass = Base64.getDecoder()
								.decode(usuario.getSenha());
			if (new String(decodedPass).equals(senha)) {
				return usuario;
			}
		}
	 }
		return null;
	}
	
	@Transactional
	public Usuario inativar(long id) {
		Optional<Usuario> _usuario = 
				usuarioRepository.findById(id);
		
		if (_usuario.isPresent()) {
			Usuario usuarioAtualizada = _usuario.get();
			usuarioAtualizada.setStatusUsuario("INATIVO");
			
			return usuarioRepository.save(usuarioAtualizada);
		}
		return null;
	}
	
	@Transactional
	public Usuario inativoPeloUsuario(long id) {
		Optional<Usuario> _usuario = 
				usuarioRepository.findById(id);
		
		if (_usuario.isPresent()) {
			Usuario usuarioAtualizada = _usuario.get();
			usuarioAtualizada.setStatusUsuario("inativoPeloUsuario");
			
			return usuarioRepository.save(usuarioAtualizada);
		}
		return null;
	}
	
	@Transactional
	public Usuario reativar(long id) {
		Optional<Usuario> _usuario = 
				usuarioRepository.findById(id);
		
		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();
			usuarioAtualizado.setStatusUsuario("ATIVO");
			
			return usuarioRepository.save(usuarioAtualizado);
		}
		return null;
	}
	
	@Transactional
	public Usuario alterarSenha(long id, String novaSenha) {
		Optional<Usuario> _usuario = 
				usuarioRepository.findById(id);
		
		if (_usuario.isPresent()) {
			Usuario usuarioAtualizado = _usuario.get();
			String senha = Base64.getEncoder()
					.encodeToString(novaSenha.getBytes());
				
			usuarioAtualizado.setSenha(senha);
			usuarioAtualizado.setStatusUsuario("ATIVO");
			
			return usuarioRepository.save(usuarioAtualizado);
		}
		return null;
	}
	

	
}
