/**
 * main.js
 * Scripts principais do sistema NiceCinema
 */

document.addEventListener("DOMContentLoaded", () => {

    /* --- Lógica do Botão Sanduíche (Header) --- */
    const toggleBtn = document.querySelector('.toggle-sidebar-btn');
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            document.body.classList.toggle('toggle-sidebar');
        });
    }

    /* --- Lógica do Sensor de Toque (Borda Esquerda) --- */
    const sensor = document.getElementById('sidebar-sensor');
    if (sensor) {
        sensor.addEventListener('mouseenter', () => {
            // Apenas ABRE o menu (adiciona a classe), não fecha se clicar de novo
            // (pois a sidebar vai cobrir o sensor quando estiver aberta)
            document.body.classList.add('toggle-sidebar');
        });

        // Adiciona suporte a 'touchstart' para resposta mais rápida no celular
        sensor.addEventListener('touchstart', (e) => {
            e.preventDefault(); // Evita comportamento padrão de clique duplo/zoom
            document.body.classList.add('toggle-sidebar');
        }, {passive: false});
    }

});