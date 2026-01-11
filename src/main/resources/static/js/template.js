/* ========================================
   TEMPLATE JS - ULTRAFLY COMPONENTS
   Scripts pour composants réutilisables
   ======================================== */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all components
    initSidebar();
    initNavbar();
    initTable();
    initCheckboxDivs();
});

/* ========================================
   SIDEBAR
   ======================================== */
function initSidebar() {
    const sidebar = document.querySelector('.uf-sidebar');
    const toggleBtn = document.querySelector('.uf-sidebar-toggle');
    const mainContent = document.querySelector('.uf-main');
    
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            if (mainContent) {
                mainContent.classList.toggle('expanded');
            }
            
            // Update toggle icon
            const icon = toggleBtn.querySelector('span') || toggleBtn;
            if (sidebar.classList.contains('collapsed')) {
                icon.textContent = '→';
            } else {
                icon.textContent = '←';
            }
        });
    }
    
    // Set active link based on current page
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.uf-sidebar-link');
    
    sidebarLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.includes(href.replace(/^\//, ''))) {
            link.classList.add('active');
        }
    });
}

/* ========================================
   NAVBAR
   ======================================== */
function initNavbar() {
    const navLinks = document.querySelectorAll('.uf-navbar-link');
    const currentPath = window.location.pathname;
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.includes(href.replace(/^\//, ''))) {
            link.classList.add('active');
        }
        
        link.addEventListener('click', function() {
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });
}

/* ========================================
   TABLE SORTING
   ======================================== */
function initTable() {
    const tables = document.querySelectorAll('.uf-table');
    
    tables.forEach(table => {
        const headers = table.querySelectorAll('th.sortable');
        
        headers.forEach((header, index) => {
            header.addEventListener('click', function() {
                const isAsc = this.classList.contains('asc');
                const isDesc = this.classList.contains('desc');
                
                // Reset all headers
                headers.forEach(h => {
                    h.classList.remove('asc', 'desc');
                });
                
                // Set new sort direction
                if (!isAsc && !isDesc) {
                    this.classList.add('asc');
                    sortTable(table, index, 'asc');
                } else if (isAsc) {
                    this.classList.add('desc');
                    sortTable(table, index, 'desc');
                } else {
                    this.classList.add('asc');
                    sortTable(table, index, 'asc');
                }
            });
        });
    });
}

function sortTable(table, columnIndex, direction) {
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    const header = table.querySelectorAll('th')[columnIndex];
    const isNumeric = header.classList.contains('sortable-numeric');
    const isDate = header.classList.contains('sortable-date');
    
    rows.sort((a, b) => {
        let aValue = a.cells[columnIndex].textContent.trim();
        let bValue = b.cells[columnIndex].textContent.trim();
        
        if (isNumeric) {
            aValue = parseFloat(aValue.replace(/[^\d.-]/g, '')) || 0;
            bValue = parseFloat(bValue.replace(/[^\d.-]/g, '')) || 0;
        } else if (isDate) {
            aValue = new Date(aValue).getTime() || 0;
            bValue = new Date(bValue).getTime() || 0;
        } else {
            aValue = aValue.toLowerCase();
            bValue = bValue.toLowerCase();
        }
        
        if (direction === 'asc') {
            return aValue > bValue ? 1 : aValue < bValue ? -1 : 0;
        } else {
            return aValue < bValue ? 1 : aValue > bValue ? -1 : 0;
        }
    });
    
    // Re-append sorted rows
    rows.forEach(row => tbody.appendChild(row));
}

/* ========================================
   CHECKBOX DIVS (Click to select)
   ======================================== */
function initCheckboxDivs() {
    const checkboxItems = document.querySelectorAll('.uf-form-checkbox-item');
    
    checkboxItems.forEach(item => {
        item.addEventListener('click', function() {
            this.classList.toggle('selected');
            
            // Update icon
            const icon = this.querySelector('.uf-form-checkbox-icon');
            if (icon) {
                if (this.classList.contains('selected')) {
                    icon.textContent = '✓';
                } else {
                    icon.textContent = '';
                }
            }
            
            // Update hidden input if exists
            const hiddenInput = this.querySelector('input[type="hidden"]');
            if (hiddenInput) {
                hiddenInput.value = this.classList.contains('selected') ? 'true' : 'false';
            }
        });
    });
}

/* ========================================
   UTILITY FUNCTIONS
   ======================================== */

// Function to get all selected checkbox values
function getSelectedCheckboxValues(groupClass) {
    const selectedItems = document.querySelectorAll('.' + groupClass + ' .uf-form-checkbox-item.selected');
    const values = [];
    selectedItems.forEach(item => {
        const value = item.getAttribute('data-value');
        if (value) {
            values.push(value);
        }
    });
    return values;
}

// Function to set checkbox selected state programmatically
function setCheckboxSelected(item, selected) {
    if (selected) {
        item.classList.add('selected');
        const icon = item.querySelector('.uf-form-checkbox-icon');
        if (icon) icon.textContent = '✓';
    } else {
        item.classList.remove('selected');
        const icon = item.querySelector('.uf-form-checkbox-icon');
        if (icon) icon.textContent = '';
    }
}

/* ========================================
   DYNAMIC TABLE ROWS (Form)
   ======================================== */
function addRow() {
    const table = document.getElementById('dynamicTable');
    if (!table) return;
    
    const tbody = table.querySelector('tbody');
    const addRowElement = tbody.querySelector('.uf-form-table-add-row');
    
    const newRow = document.createElement('tr');
    newRow.className = 'uf-form-table-row';
    newRow.innerHTML = `
        <td><input type="text" name="dynamicField[]" placeholder="------" class="uf-form-input"></td>
        <td class="uf-form-table-action">
            <button type="button" class="uf-form-btn-delete" onclick="removeRow(this)">✕</button>
        </td>
    `;
    
    tbody.insertBefore(newRow, addRowElement);
}

function removeRow(btn) {
    const row = btn.closest('tr');
    const table = row.closest('table');
    const rows = table.querySelectorAll('.uf-form-table-row');
    
    // Keep at least one row
    if (rows.length > 1) {
        row.remove();
    }
}
