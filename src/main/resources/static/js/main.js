document.addEventListener("DOMContentLoaded", () => {

    /* --- Botão Sanduíche (Clique) --- */
    const toggleBtn = document.querySelector('.toggle-sidebar-btn');
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            document.body.classList.toggle('toggle-sidebar');
        });
    }

    /* --- Sensor Lateral (Passar o Mouse) --- */
    const sensor = document.getElementById('sidebar-sensor');

    if (sensor) {
        // Evento: Quando o mouse ENTRA na área (encosta na borda)
        sensor.addEventListener('mouseenter', () => {

            // Lógica Inteligente:
            // Queremos MOSTRAR a sidebar.

            // Cenário 1: Mobile (Tela pequena)
            // No mobile, a classe 'toggle-sidebar' MOSTRA o menu.
            if (window.innerWidth < 1200) {
                document.body.classList.add('toggle-sidebar');
            }

                // Cenário 2: Desktop (Tela grande)
                // No desktop, a sidebar já é visível por padrão.
                // A classe 'toggle-sidebar' ESCONDE o menu.
            // Então, se estiver escondido (tem a classe), nós removemos a classe para mostrar.
            else {
                if (document.body.classList.contains('toggle-sidebar')) {
                    document.body.classList.remove('toggle-sidebar');
                }
            }
        });
    }

});