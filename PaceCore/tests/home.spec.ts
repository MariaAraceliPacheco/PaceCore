import { test, expect } from "@playwright/test";

test('la aplicacion carga correctamente', async ({ page }) => {
    await page.goto('http://localhost:4200');

    // 2. Comprobar que la página tiene un título
  await expect(page).toHaveTitle(/PaceCore/i);

  // 3. Comprobar que aparece el eslogan principal de la home
  await expect(page.getByRole('heading', { name: /RUN ON/i })).toBeVisible();
})

test('puedo hacer click en un boton del home', async ({page}) => {
    await page.goto('http://localhost:4200')

    await page.getByRole('navigation').getByRole('link', { name: 'Iniciar Sesión' }).click();

    await expect(page).toHaveURL(/login/i)
})

test('puedo hacer login', async ({page}) => {
    await page.goto('http://localhost:4200/login');

    await page.getByLabel('Email').fill('araceli@gmail.com');
    await page.getByLabel('Contraseña').fill('araceli@gmail.com');
    await page.getByRole('button', {name: 'Iniciar Sesión'}).click(); //importante hacer click en el boton para iniciar sesion

    await expect(page).toHaveURL(/dashboard/i);
})