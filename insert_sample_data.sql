-- Sample Data Insertion for Donation Management System

-- 1. Insert Donors
INSERT INTO donors (name, contact_info, created_at, updated_at) VALUES
('John Smith', 'Phone: 555-0101\nEmail: john.smith@email.com\nAddress: 123 Main St, New York, NY 10001', NOW(), NOW()),
('Sarah Johnson', 'Phone: 555-0102\nEmail: sarah.j@email.com\nAddress: 456 Oak Ave, Los Angeles, CA 90210', NOW(), NOW()),
('Mike Wilson', 'Phone: 555-0103\nEmail: mike.wilson@email.com\nAddress: 789 Pine St, Chicago, IL 60601', NOW(), NOW()),
('Emily Davis', 'Phone: 555-0104\nEmail: emily.davis@email.com\nAddress: 321 Elm St, Houston, TX 77001', NOW(), NOW()),
('David Brown', 'Phone: 555-0105\nEmail: david.brown@email.com\nAddress: 654 Maple Dr, Phoenix, AZ 85001', NOW(), NOW()),
('Lisa Garcia', 'Phone: 555-0106\nEmail: lisa.garcia@email.com\nAddress: 987 Cedar Ln, Philadelphia, PA 19101', NOW(), NOW()),
('Robert Miller', 'Phone: 555-0107\nEmail: robert.miller@email.com\nAddress: 147 Birch St, San Antonio, TX 78201', NOW(), NOW()),
('Jennifer Martinez', 'Phone: 555-0108\nEmail: jennifer.martinez@email.com\nAddress: 258 Spruce Ave, San Diego, CA 92101', NOW(), NOW()),
('William Anderson', 'Phone: 555-0109\nEmail: william.anderson@email.com\nAddress: 369 Willow Way, Dallas, TX 75201', NOW(), NOW()),
('Maria Rodriguez', 'Phone: 555-0110\nEmail: maria.rodriguez@email.com\nAddress: 741 Poplar Blvd, San Jose, CA 95101', NOW(), NOW());

-- 2. Insert All Donations First (Join donors by name, no hardcoded IDs)
INSERT INTO donations (donor_id, donation_type, quantity, unit, description, donation_date, notes, created_at, updated_at)
SELECT d.id, v.donation_type, v.quantity, v.unit, v.description, (v.donation_date::timestamp), v.notes, NOW(), NOW()
FROM (
  VALUES
  ('John Smith','money',500.00,'dollars','Cash donation for emergency relief fund','2024-01-15 10:30:00','Thank you for your generous contribution'),
  ('Sarah Johnson','food',25.50,'kg','Canned goods and non-perishable food items','2024-01-16 14:20:00','Mixed canned vegetables and soups'),
  ('Mike Wilson','clothing',15,'pieces','Winter clothing for homeless shelter','2024-01-17 09:15:00','Coats, sweaters, and warm clothing'),
  ('Emily Davis','medicine',8,'pieces','Prescription medications and first aid supplies','2024-01-18 16:45:00','Various prescription medications'),
  ('David Brown','money',1000.00,'dollars','Large cash donation for building repairs','2024-01-19 11:00:00','Designated for facility improvements'),
  ('Lisa Garcia','furniture',3,'pieces','Office furniture for community center','2024-01-20 13:30:00','Desks, chairs, and filing cabinets'),
  ('Robert Miller','books',50,'pieces','Educational books for children','2024-01-21 15:20:00','Children''s books and educational materials'),
  ('Jennifer Martinez','toys',20,'pieces','Toys for children in need','2024-01-22 12:10:00','Stuffed animals and educational toys'),
  ('William Anderson','food',40.25,'kg','Fresh produce and dairy products','2024-01-23 08:45:00','Fresh vegetables and dairy items'),
  ('Maria Rodriguez','money',250.00,'dollars','General fund donation','2024-01-24 17:30:00','Unrestricted donation for general use'),
  ('John Smith','clothing',8,'pieces','Summer clothing donation','2024-01-25 14:15:00','T-shirts, shorts, and summer wear'),
  ('Sarah Johnson','medicine',12,'pieces','Over-the-counter medications','2024-01-26 10:30:00','Pain relievers and cold medicines'),
  ('Mike Wilson','food',30.75,'kg','Bulk food items for food bank','2024-01-27 16:20:00','Rice, pasta, and canned goods'),
  ('Emily Davis','books',25,'pieces','Adult fiction and non-fiction books','2024-01-28 11:45:00','Various genres for adult readers'),
  ('David Brown','toys',15,'pieces','Board games and puzzles','2024-01-29 13:00:00','Educational games for all ages'),
  ('Lisa Garcia','money',750.00,'dollars','Holiday season donation','2024-02-01 12:00:00','Special holiday contribution'),
  ('Robert Miller','food',18.50,'kg','Holiday meal ingredients','2024-02-02 14:30:00','Turkey, ham, and side dishes'),
  ('Jennifer Martinez','clothing',22,'pieces','Professional clothing for job interviews','2024-02-03 10:15:00','Suits, dresses, and professional attire'),
  ('William Anderson','medicine',5,'pieces','Specialized medical equipment','2024-02-04 16:45:00','Blood pressure monitors and thermometers'),
  ('Maria Rodriguez','furniture',1,'pieces','Large conference table','2024-02-05 11:20:00','Wooden conference table for meetings')
) AS v(donor_name, donation_type, quantity, unit, description, donation_date, notes)
JOIN donors d ON d.name = v.donor_name;

-- 3. Insert Distributions (Join donations by description + donation_date)
INSERT INTO distributions (donation_id, quantity_distributed, recipient_name, recipient_contact, recipient_info, distribution_date, notes, created_at, updated_at)
SELECT dn.id, v.quantity_distributed, v.recipient_name, v.recipient_contact, v.recipient_info, (v.distribution_date::timestamp), v.notes, NOW(), NOW()
FROM (
  VALUES
  ('Cash donation for emergency relief fund','2024-01-15 10:30:00',500.00,'Emergency Relief Fund','Phone: 555-0201\nEmail: relief@charity.org','Name: Emergency Relief Fund\nContact: Phone: 555-0201\nEmail: relief@charity.org','2024-01-16 09:00:00','Funds used for emergency housing assistance'),
  ('Canned goods and non-perishable food items','2024-01-16 14:20:00',20.00,'Food Bank Distribution','Phone: 555-0202\nEmail: foodbank@charity.org','Name: Food Bank Distribution\nContact: Phone: 555-0202\nEmail: foodbank@charity.org','2024-01-17 10:30:00','Distributed to local food bank'),
  ('Winter clothing for homeless shelter','2024-01-17 09:15:00',12,'Homeless Shelter','Phone: 555-0203\nEmail: shelter@charity.org','Name: Homeless Shelter\nContact: Phone: 555-0203\nEmail: shelter@charity.org','2024-01-18 14:15:00','Winter clothing distributed to residents'),
  ('Prescription medications and first aid supplies','2024-01-18 16:45:00',6,'Community Health Center','Phone: 555-0204\nEmail: health@charity.org','Name: Community Health Center\nContact: Phone: 555-0204\nEmail: health@charity.org','2024-01-19 11:20:00','Medications provided to low-income patients'),
  ('Large cash donation for building repairs','2024-01-19 11:00:00',800.00,'Building Maintenance Fund','Phone: 555-0205\nEmail: maintenance@charity.org','Name: Building Maintenance Fund\nContact: Phone: 555-0205\nEmail: maintenance@charity.org','2024-01-20 16:45:00','Used for roof repairs and HVAC maintenance'),
  ('Office furniture for community center','2024-01-20 13:30:00',2,'Community Center','Phone: 555-0206\nEmail: community@charity.org','Name: Community Center\nContact: Phone: 555-0206\nEmail: community@charity.org','2024-01-21 13:30:00','Office furniture for new community programs'),
  ('Educational books for children','2024-01-21 15:20:00',35,'Children''s Library','Phone: 555-0207\nEmail: library@charity.org','Name: Children''s Library\nContact: Phone: 555-0207\nEmail: library@charity.org','2024-01-22 15:00:00','Books added to children''s reading program'),
  ('Toys for children in need','2024-01-22 12:10:00',15,'Foster Care Program','Phone: 555-0208\nEmail: foster@charity.org','Name: Foster Care Program\nContact: Phone: 555-0208\nEmail: foster@charity.org','2024-01-23 12:15:00','Toys distributed to foster children'),
  ('Fresh produce and dairy products','2024-01-23 08:45:00',30.00,'Senior Center','Phone: 555-0209\nEmail: seniors@charity.org','Name: Senior Center\nContact: Phone: 555-0209\nEmail: seniors@charity.org','2024-01-24 10:45:00','Fresh produce for senior meal program'),
  ('General fund donation','2024-01-24 17:30:00',200.00,'General Operations','Phone: 555-0210\nEmail: operations@charity.org','Name: General Operations\nContact: Phone: 555-0210\nEmail: operations@charity.org','2024-01-25 14:30:00','Used for general operational expenses'),
  ('Summer clothing donation','2024-01-25 14:15:00',6,'Youth Center','Phone: 555-0211\nEmail: youth@charity.org','Name: Youth Center\nContact: Phone: 555-0211\nEmail: youth@charity.org','2024-01-26 16:20:00','Summer clothing for youth programs'),
  ('Over-the-counter medications','2024-01-26 10:30:00',8,'Free Clinic','Phone: 555-0212\nEmail: clinic@charity.org','Name: Free Clinic\nContact: Phone: 555-0212\nEmail: clinic@charity.org','2024-01-27 11:30:00','OTC medications for uninsured patients'),
  ('Bulk food items for food bank','2024-01-27 16:20:00',25.00,'Food Pantry','Phone: 555-0213\nEmail: pantry@charity.org','Name: Food Pantry\nContact: Phone: 555-0213\nEmail: pantry@charity.org','2024-01-28 09:15:00','Bulk food items for weekly distribution'),
  ('Adult fiction and non-fiction books','2024-01-28 11:45:00',20,'Adult Education Center','Phone: 555-0214\nEmail: education@charity.org','Name: Adult Education Center\nContact: Phone: 555-0214\nEmail: education@charity.org','2024-01-29 13:45:00','Books for adult literacy program'),
  ('Board games and puzzles','2024-01-29 13:00:00',10,'Family Services','Phone: 555-0215\nEmail: family@charity.org','Name: Family Services\nContact: Phone: 555-0215\nEmail: family@charity.org','2024-01-30 15:30:00','Games for family counseling sessions'),
  ('Holiday season donation','2024-02-01 12:00:00',600.00,'Holiday Assistance Program','Phone: 555-0216\nEmail: holiday@charity.org','Name: Holiday Assistance Program\nContact: Phone: 555-0216\nEmail: holiday@charity.org','2024-02-06 10:00:00','Funds for holiday gift program'),
  ('Holiday meal ingredients','2024-02-02 14:30:00',15.00,'Community Kitchen','Phone: 555-0217\nEmail: kitchen@charity.org','Name: Community Kitchen\nContact: Phone: 555-0217\nEmail: kitchen@charity.org','2024-02-07 13:30:00','Holiday meal preparation'),
  ('Professional clothing for job interviews','2024-02-03 10:15:00',18,'Job Training Center','Phone: 555-0218\nEmail: training@charity.org','Name: Job Training Center\nContact: Phone: 555-0218\nEmail: training@charity.org','2024-02-08 15:45:00','Professional clothing for job seekers'),
  ('Specialized medical equipment','2024-02-04 16:45:00',3,'Senior Health Program','Phone: 555-0219\nEmail: seniorhealth@charity.org','Name: Senior Health Program\nContact: Phone: 555-0219\nEmail: seniorhealth@charity.org','2024-02-09 11:15:00','Medical equipment for elderly care'),
  ('Large conference table','2024-02-05 11:20:00',1,'Board Meeting Room','Phone: 555-0220\nEmail: board@charity.org','Name: Board Meeting Room\nContact: Phone: 555-0220\nEmail: board@charity.org','2024-02-10 14:00:00','Conference table for board meetings')
) AS v(description, donation_date, quantity_distributed, recipient_name, recipient_contact, recipient_info, distribution_date, notes)
JOIN donations dn ON dn.description = v.description AND dn.donation_date = (v.donation_date::timestamp);

