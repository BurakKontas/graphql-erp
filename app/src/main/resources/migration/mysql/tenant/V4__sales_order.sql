-- MySQL adapted version of V4__sales_order.sql

CREATE TABLE IF NOT EXISTS sales_orders (
  id VARCHAR(36) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  company_id VARCHAR(36) NOT NULL,
  order_number VARCHAR(20) NOT NULL,
  order_date DATE NOT NULL,
  expiry_date DATE,
  customer_id VARCHAR(36),
  currency_code VARCHAR(3),
  payment_term_code VARCHAR(50),
  shipping_address_line1 VARCHAR(500),
  shipping_address_line2 VARCHAR(500),
  shipping_city VARCHAR(255),
  shipping_state_or_province VARCHAR(255),
  shipping_postal_code VARCHAR(50),
  shipping_country_code VARCHAR(2),
  status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
  fulfillment_status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED',
  invoiced_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
  subtotal DECIMAL(19,2) NOT NULL DEFAULT 0,
  tax_total DECIMAL(19,2) NOT NULL DEFAULT 0,
  total DECIMAL(19,2) NOT NULL DEFAULT 0,
  CONSTRAINT fk_sales_order_company FOREIGN KEY (company_id) REFERENCES companies (id),
  CONSTRAINT fk_sales_order_customer FOREIGN KEY (customer_id) REFERENCES business_partner (id),
  CONSTRAINT fk_sales_order_currency FOREIGN KEY (currency_code) REFERENCES currencies (code),
  CONSTRAINT fk_sales_order_payment_term FOREIGN KEY (payment_term_code) REFERENCES payment_terms (code),
  UNIQUE KEY uq_sales_order_number (tenant_id, order_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_sales_order_tenant_id ON sales_orders (tenant_id);
CREATE INDEX idx_sales_order_company_id ON sales_orders (company_id);
CREATE INDEX idx_sales_order_customer_id ON sales_orders (customer_id);
CREATE INDEX idx_sales_order_status ON sales_orders (status);
CREATE INDEX idx_sales_order_order_date ON sales_orders (order_date);

CREATE TABLE IF NOT EXISTS sales_order_lines (
  id VARCHAR(36) PRIMARY KEY,
  order_id VARCHAR(36) NOT NULL,
  sequence INT NOT NULL,
  item_id VARCHAR(255),
  item_description VARCHAR(500) NOT NULL,
  unit_code VARCHAR(50) NOT NULL,
  quantity DECIMAL(19,4) NOT NULL,
  unit_price DECIMAL(19,6) NOT NULL,
  tax_code VARCHAR(50),
  tax_rate DECIMAL(10,4),
  line_total DECIMAL(19,2) NOT NULL,
  tax_amount DECIMAL(19,2) NOT NULL,
  line_total_with_tax DECIMAL(19,2) NOT NULL,
  CONSTRAINT fk_sales_order_line_order FOREIGN KEY (order_id) REFERENCES sales_orders (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_sales_order_line_order_id ON sales_order_lines (order_id);

-- Permissions for SALES_ORDER may be inserted in V3 reference data already

